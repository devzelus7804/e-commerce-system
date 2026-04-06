package DAJ2EE.repository;

import DAJ2EE.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByIsDeletedFalse();
    Optional<Cart> findByIdAndIsDeletedFalse(Long id);
    Optional<Cart> findByUserIdAndIsDeletedFalse(Long userId);
    List<Cart> findByUserId(Long userId);
}
