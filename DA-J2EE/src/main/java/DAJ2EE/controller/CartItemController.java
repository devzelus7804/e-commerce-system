package DAJ2EE.Controller;

import DAJ2EE.entity.CartItem;
import DAJ2EE.Service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @GetMapping
    public List<CartItem> getAll() {
        return cartItemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItem> getById(@PathVariable Long id) {
        return cartItemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cart/{cartId}")
    public List<CartItem> getByCartId(@PathVariable Long cartId) {
        return cartItemService.findByCartId(cartId);
    }
}
