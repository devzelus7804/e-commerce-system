package DAJ2EE.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;

/**
 * MoMo Payment Service using ATM / QR checkout flow (paymentCode).
 * Sandbox credentials from https://developers.momo.vn
 */
@Service
public class MomoPaymentService {

    // ─── Sandbox test credentials (from developers.momo.vn) ───────────────────
    @Value("${momo.partner-code:MOMOATM4}")
    private String partnerCode;

    @Value("${momo.access-key:F8BBA842ECF85}")
    private String accessKey;

    @Value("${momo.secret-key:K951B6PE1waDMi640xX08PD3vg6EkVlz}")
    private String secretKey;

    @Value("${momo.endpoint:https://test-payment.momo.vn/v2/gateway/api/create}")
    private String endpoint;

    @Value("${momo.redirect-url:http://localhost:8080/payment-result}")
    private String redirectUrl;

    @Value("${momo.ipn-url:http://localhost:8080/api/momo/ipn}")
    private String ipnUrl;

    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Build the MoMo payment initiation request body.
     *
     * @param orderId     Your system's order ID
     * @param amount      Amount in VND (no decimals)
     * @param orderInfo   Human readable order description
     * @return map with all fields required by MoMo API + the generated signature
     */
    public Map<String, Object> buildPaymentRequest(String orderId, long amount, String orderInfo) throws Exception {
        String requestId = UUID.randomUUID().toString();
        String requestType = "captureWallet";
        String extraData = "";

        // Build raw signature string (exactly as per MoMo v2 docs)
        String rawSignature = "accessKey=" + accessKey
                + "&amount=" + amount
                + "&extraData=" + extraData
                + "&ipnUrl=" + ipnUrl
                + "&orderId=" + orderId
                + "&orderInfo=" + orderInfo
                + "&partnerCode=" + partnerCode
                + "&redirectUrl=" + redirectUrl
                + "&requestId=" + requestId
                + "&requestType=" + requestType;

        String signature = hmacSha256(rawSignature, secretKey);

        return Map.ofEntries(
                Map.entry("partnerCode", partnerCode),
                Map.entry("accessKey", accessKey),
                Map.entry("requestId", requestId),
                Map.entry("amount", String.valueOf(amount)),
                Map.entry("orderId", orderId),
                Map.entry("orderInfo", orderInfo),
                Map.entry("redirectUrl", redirectUrl),
                Map.entry("ipnUrl", ipnUrl),
                Map.entry("extraData", extraData),
                Map.entry("requestType", requestType),
                Map.entry("signature", signature),
                Map.entry("lang", "vi")
        );
    }

    /**
     * Verify MoMo IPN / return callback signature.
     */
    public boolean verifySignature(Map<String, String> params, String receivedSignature) throws Exception {
        String rawSignature = "accessKey=" + accessKey
                + "&amount=" + params.getOrDefault("amount", "")
                + "&extraData=" + params.getOrDefault("extraData", "")
                + "&message=" + params.getOrDefault("message", "")
                + "&orderId=" + params.getOrDefault("orderId", "")
                + "&orderInfo=" + params.getOrDefault("orderInfo", "")
                + "&orderType=" + params.getOrDefault("orderType", "")
                + "&partnerCode=" + params.getOrDefault("partnerCode", "")
                + "&payType=" + params.getOrDefault("payType", "")
                + "&requestId=" + params.getOrDefault("requestId", "")
                + "&responseTime=" + params.getOrDefault("responseTime", "")
                + "&resultCode=" + params.getOrDefault("resultCode", "")
                + "&transId=" + params.getOrDefault("transId", "");

        String expected = hmacSha256(rawSignature, secretKey);
        return expected.equals(receivedSignature);
    }

    private String hmacSha256(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash);
    }
}
