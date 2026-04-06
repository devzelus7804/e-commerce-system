package DAJ2EE.Service;

import DAJ2EE.entity.*;
import DAJ2EE.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    /**
     * Create a new order from the user's current cart.
     */
    @Transactional
    public Order createOrderFromCart(Long userId, String receiverName, String receiverPhone,
                                     String shippingAddress, String note, String paymentMethod) {
        Cart cart = cartRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        List<CartItem> cartItems = cartItemRepository.findByCartIdAndIsDeletedFalse(cart.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        BigDecimal total = cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setTotalAmount(total);
        order.setStatus("PENDING");
        order.setPaymentMethod(paymentMethod != null ? paymentMethod : "MOMO");
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setShippingAddress(shippingAddress);
        order.setNote(note);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setIsDeleted(false);
        Order savedOrder = orderRepository.save(order);

        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setOrder(savedOrder);
            oi.setProductVariant(ci.getProductVariant());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getProductVariant().getPrice());
            oi.setTotalPrice(ci.getTotalPrice());
            oi.setCreatedAt(LocalDateTime.now());
            oi.setUpdatedAt(LocalDateTime.now());
            oi.setIsDeleted(false);
            orderItemRepository.save(oi);
            
            // Clear item from cart
            ci.setIsDeleted(true);
            ci.setDeletedAt(LocalDateTime.now());
            cartItemRepository.save(ci);
        }

        return savedOrder;
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findByIdAndIsDeletedFalse(id);
    }

    public Optional<Order> findByMomoOrderId(String momoOrderId) {
        return orderRepository.findByMomoOrderIdAndIsDeletedFalse(momoOrderId);
    }

    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(userId);
    }

    public List<Order> findAll() {
        return orderRepository.findByIsDeletedFalse();
    }

    public Order save(Order order) {
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    /**
     * Called after MoMo IPN, update order status.
     */
    @Transactional
    public void updatePaymentResult(String momoOrderId, String transId, int resultCode) {
        Order order = findByMomoOrderId(momoOrderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + momoOrderId));
        order.setMomoTransId(transId);
        order.setStatus(resultCode == 0 ? "PAID" : "FAILED");
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }
}
