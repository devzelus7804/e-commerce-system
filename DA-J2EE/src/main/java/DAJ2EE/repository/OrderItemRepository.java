package DAJ2EE.repository;

import DAJ2EE.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderIdAndIsDeletedFalse(Long orderId);
}
