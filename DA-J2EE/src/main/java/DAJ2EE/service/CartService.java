package DAJ2EE.Service;

import DAJ2EE.entity.Cart;
import DAJ2EE.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public List<Cart> findAll() {
        return cartRepository.findByIsDeletedFalse();
    }

    public Optional<Cart> findById(Long id) {
        return cartRepository.findByIdAndIsDeletedFalse(id);
    }

    public Optional<Cart> findByUserId(Long userId) {
        return cartRepository.findByUserIdAndIsDeletedFalse(userId);
    }

    public Cart save(Cart cart) {
        if (cart.getCreatedAt() == null) {
            cart.setCreatedAt(LocalDateTime.now());
        }
        cart.setUpdatedAt(LocalDateTime.now());
        cart.setIsDeleted(false);
        return cartRepository.save(cart);
    }

    public Cart update(Long id, Cart cartDetails) {
        Cart cart = findById(id).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setUser(cartDetails.getUser());
        cart.setUpdatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    public void delete(Long id) {
        Cart cart = findById(id).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setIsDeleted(true);
        cart.setDeletedAt(LocalDateTime.now());
        
        // Cascading soft delete for cart items
        if (cart.getId() != null) {
            // Items will be deleted through CartItemService
        }
        
        cartRepository.save(cart);
    }
}
