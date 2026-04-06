package DAJ2EE.Service;

import DAJ2EE.entity.CartItem;
import DAJ2EE.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public List<CartItem> findAll() {
        return cartItemRepository.findByIsDeletedFalse();
    }

    public Optional<CartItem> findById(Long id) {
        return cartItemRepository.findByIdAndIsDeletedFalse(id);
    }

    public List<CartItem> findByCartId(Long cartId) {
        return cartItemRepository.findByCartIdAndIsDeletedFalse(cartId);
    }

    public CartItem save(CartItem cartItem) {
        if (cartItem.getCreatedAt() == null) {
            cartItem.setCreatedAt(LocalDateTime.now());
        }
        cartItem.setUpdatedAt(LocalDateTime.now());
        cartItem.setIsDeleted(false);
        return cartItemRepository.save(cartItem);
    }

    public CartItem update(Long id, CartItem itemDetails) {
        CartItem cartItem = findById(id).orElseThrow(() -> new RuntimeException("CartItem not found"));
        cartItem.setCart(itemDetails.getCart());
        cartItem.setProductVariant(itemDetails.getProductVariant());
        cartItem.setQuantity(itemDetails.getQuantity());
        cartItem.setTotalPrice(itemDetails.getTotalPrice());
        cartItem.setUpdatedAt(LocalDateTime.now());
        return cartItemRepository.save(cartItem);
    }

    public void delete(Long id) {
        CartItem cartItem = findById(id).orElseThrow(() -> new RuntimeException("CartItem not found"));
        cartItem.setIsDeleted(true);
        cartItem.setDeletedAt(LocalDateTime.now());
        cartItemRepository.save(cartItem);
    }

    public Optional<CartItem> findByCartAndVariant(Long cartId, Long productVariantId) {
        return cartItemRepository.findByCartIdAndProductVariantIdAndIsDeletedFalse(cartId, productVariantId);
    }
}
