package DAJ2EE.repository;

import DAJ2EE.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(Long userId);
    Optional<Order> findByIdAndIsDeletedFalse(Long id);
    Optional<Order> findByMomoOrderIdAndIsDeletedFalse(String momoOrderId);
    List<Order> findByIsDeletedFalse();
}
