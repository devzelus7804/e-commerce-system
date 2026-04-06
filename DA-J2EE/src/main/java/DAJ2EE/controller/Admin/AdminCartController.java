package DAJ2EE.Controller.Admin;

import DAJ2EE.entity.Cart;
import DAJ2EE.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/carts")
public class AdminCartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public List<Cart> getAll() {
        return cartService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getById(@PathVariable Long id) {
        return cartService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Cart create(@RequestBody Cart cart) {
        return cartService.save(cart);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cart> update(@PathVariable Long id, @RequestBody Cart cartDetails) {
        try {
            return ResponseEntity.ok(cartService.update(id, cartDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            cartService.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
