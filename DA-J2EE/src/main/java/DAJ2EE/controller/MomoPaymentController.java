package DAJ2EE.Controller;

import DAJ2EE.Service.MomoPaymentService;
import DAJ2EE.Service.OrderService;
import DAJ2EE.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/momo")
public class MomoPaymentController {

    @Autowired
    private MomoPaymentService momoPaymentService;

    @Autowired
    private OrderService orderService;

    @Value("${momo.endpoint:https://test-payment.momo.vn/v2/gateway/api/create}")
    private String momoEndpoint;

    /**
     * POST /api/momo/create-payment
     * Body: { userId, receiverName, receiverPhone, shippingAddress, note }
     *
     * Creates an order from the user's cart, calls MoMo API, and returns payUrl.
     */
    @PostMapping("/create-payment")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody Map<String, Object> body) {
        try {
            Long userId = Long.valueOf(body.get("userId").toString());
            String receiverName    = body.getOrDefault("receiverName", "").toString();
            String receiverPhone   = body.getOrDefault("receiverPhone", "").toString();
            String shippingAddress = body.getOrDefault("shippingAddress", "").toString();
            String note            = body.getOrDefault("note", "").toString();

            // 1. Create order from cart
            Order order = orderService.createOrderFromCart(
                    userId, receiverName, receiverPhone, shippingAddress, note, "MOMO");

            // 2. Generate MoMo orderId
            String momoOrderId = "VELA-" + order.getId() + "-" + System.currentTimeMillis();
            long amount = order.getTotalAmount().longValue();
            String orderInfo = "VELA Fashion - Don hang #" + order.getId();

            // 3. Persist momoOrderId
            order.setMomoOrderId(momoOrderId);
            orderService.save(order);

            // 4. Build MoMo request payload
            Map<String, Object> momoBody = momoPaymentService.buildPaymentRequest(momoOrderId, amount, orderInfo);
            String requestId = momoBody.get("requestId").toString();

            // 5. Call MoMo API via RestTemplate
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(momoBody, headers);

            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> momoResponse =
                    (ResponseEntity<Map<String, Object>>) (ResponseEntity<?>) restTemplate.postForEntity(
                            momoEndpoint, entity, Map.class);

            Map<String, Object> momoResult = momoResponse.getBody();

            if (momoResult == null) {
                Map<String, Object> err = new HashMap<>();
                err.put("error", "No response from MoMo");
                return ResponseEntity.status(502).body(err);
            }

            String payUrl = Objects.toString(momoResult.getOrDefault("payUrl", ""), "");

            // 6. Persist payUrl and requestId
            order.setMomoPayUrl(payUrl);
            order.setMomoRequestId(requestId);
            orderService.save(order);

            Map<String, Object> result = new HashMap<>();
            result.put("orderId", order.getId());
            result.put("momoOrderId", momoOrderId);
            result.put("payUrl", payUrl);
            result.put("amount", amount);
            result.put("message", Objects.toString(momoResult.getOrDefault("message", ""), ""));
            result.put("resultCode", momoResult.getOrDefault("resultCode", -1));
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    /**
     * POST /api/momo/ipn
     * MoMo sends Instant Payment Notification when a transaction completes.
     */
    @PostMapping("/ipn")
    public ResponseEntity<Map<String, Object>> handleIpn(@RequestBody Map<String, String> ipnBody) {
        try {
            String signature = ipnBody.getOrDefault("signature", "");
            boolean valid = momoPaymentService.verifySignature(ipnBody, signature);
            if (!valid) {
                Map<String, Object> err = new HashMap<>();
                err.put("error", "Invalid signature");
                return ResponseEntity.status(400).body(err);
            }

            String momoOrderId = ipnBody.get("orderId");
            String transId     = ipnBody.getOrDefault("transId", "");
            int resultCode     = Integer.parseInt(ipnBody.getOrDefault("resultCode", "-1"));

            orderService.updatePaymentResult(momoOrderId, transId, resultCode);

            Map<String, Object> ok = new HashMap<>();
            ok.put("message", "IPN processed");
            return ResponseEntity.ok(ok);
        } catch (Exception e) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    /**
     * GET /api/momo/result?orderId=...&resultCode=...&transId=...
     * MoMo redirects user back after payment. Also used for frontend polling.
     */
    @GetMapping("/result")
    public ResponseEntity<Map<String, Object>> paymentResult(
            @RequestParam String orderId,
            @RequestParam(required = false, defaultValue = "-1") int resultCode,
            @RequestParam(required = false, defaultValue = "") String transId,
            @RequestParam(required = false, defaultValue = "") String message) {

        Map<String, Object> res = new HashMap<>();
        try {
            orderService.updatePaymentResult(orderId, transId, resultCode);
        } catch (Exception ignored) {
            // order may not exist yet during polling
        }
        Optional<Order> orderOpt = orderService.findByMomoOrderId(orderId);
        res.put("orderId", orderId);
        res.put("resultCode", resultCode);
        res.put("transId", transId);
        res.put("message", message);
        res.put("status", orderOpt.map(Order::getStatus).orElse("UNKNOWN"));
        orderOpt.ifPresent(o -> res.put("dbOrderId", o.getId()));
        return ResponseEntity.ok(res);
    }

    /**
     * GET /api/momo/order-status/{momoOrderId}
     * Frontend polls this to check payment status after redirect.
     */
    @GetMapping("/order-status/{momoOrderId}")
    public ResponseEntity<Map<String, Object>> orderStatus(@PathVariable String momoOrderId) {
        Optional<Order> opt = orderService.findByMomoOrderId(momoOrderId);
        if (opt.isEmpty()) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "Order not found");
            return ResponseEntity.notFound().build();
        }
        Order o = opt.get();
        Map<String, Object> res = new HashMap<>();
        res.put("orderId", o.getId());
        res.put("momoOrderId", momoOrderId);
        res.put("status", o.getStatus());
        res.put("totalAmount", o.getTotalAmount());
        return ResponseEntity.ok(res);
    }
}
