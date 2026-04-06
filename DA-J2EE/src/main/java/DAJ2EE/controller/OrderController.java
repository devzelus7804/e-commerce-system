package DAJ2EE.Controller;

import DAJ2EE.Service.OrderService;
import DAJ2EE.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /** GET /api/orders/user/{userId} — list orders for a user */
    @GetMapping("/user/{userId}")
    public List<Order> getByUserId(@PathVariable Long userId) {
        return orderService.findByUserId(userId);
    }

    /** GET /api/orders/{id} — single order detail */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** GET /api/orders — all orders (admin) */
    @GetMapping
    public List<Order> getAll() {
        return orderService.findAll();
    }

    /** PATCH /api/orders/{id}/cancel — cancel an order */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        return orderService.findById(id).map(order -> {
            if ("PENDING".equals(order.getStatus())) {
                order.setStatus("CANCELLED");
                orderService.save(order);
                return ResponseEntity.ok(Map.of("message", "Order cancelled", "orderId", id));
            }
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Cannot cancel order with status: " + order.getStatus()));
        }).orElse(ResponseEntity.notFound().build());
    }
}
