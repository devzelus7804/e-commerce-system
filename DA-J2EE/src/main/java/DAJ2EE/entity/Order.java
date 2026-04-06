package DAJ2EE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal totalAmount;

    /**
     * PENDING, PAID, FAILED, CANCELLED
     */
    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    /**
     * MOMO, COD, etc.
     */
    @Column(nullable = false, length = 30)
    private String paymentMethod = "MOMO";

    /** MoMo orderId sent during payment request */
    @Column(length = 100)
    private String momoOrderId;

    /** MoMo requestId sent during payment request */
    @Column(length = 100)
    private String momoRequestId;

    /** MoMo payUrl returned after payment init */
    @Column(length = 500)
    private String momoPayUrl;

    /** Transaction ID returned by MoMo callback */
    @Column(length = 100)
    private String momoTransId;

    @Column(length = 300)
    private String shippingAddress;

    @Column(length = 150)
    private String receiverName;

    @Column(length = 20)
    private String receiverPhone;

    @Column(columnDefinition = "TEXT")
    private String note;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items;
}
