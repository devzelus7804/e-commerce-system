package DAJ2EE.Controller.Admin;

import DAJ2EE.entity.CartItem;
import DAJ2EE.Service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/cart-items")
public class AdminCartItemController {

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

    @PostMapping
    public CartItem create(@RequestBody CartItem cartItem) {
        return cartItemService.save(cartItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItem> update(@PathVariable Long id, @RequestBody CartItem itemDetails) {
        try {
            return ResponseEntity.ok(cartItemService.update(id, itemDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            cartItemService.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
