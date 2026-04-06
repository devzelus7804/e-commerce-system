package DAJ2EE.repository;

import DAJ2EE.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByIsDeletedFalse();
    List<CartItem> findByCartIdAndIsDeletedFalse(Long cartId);
    Optional<CartItem> findByIdAndIsDeletedFalse(Long id);
    Optional<CartItem> findByCartIdAndProductVariantIdAndIsDeletedFalse(Long cartId, Long productVariantId);
    List<CartItem> findByCartId(Long cartId);
}
