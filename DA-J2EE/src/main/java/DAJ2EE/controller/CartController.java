package DAJ2EE.Controller;

import DAJ2EE.Service.CartItemService;
import DAJ2EE.Service.CartService;
import DAJ2EE.Service.ProductVariantService;
import DAJ2EE.Service.UserService;
import DAJ2EE.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductVariantService productVariantService;

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

    @GetMapping("/user/{userId}")
    public ResponseEntity<Cart> getByUserId(@PathVariable Long userId) {
        return cartService.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/carts/user/{userId}/items
     * Returns all cart items for a user (auto-creates cart if missing).
     */
    @GetMapping("/user/{userId}/items")
    public ResponseEntity<?> getCartItems(@PathVariable Long userId) {
        Cart cart = getOrCreateCart(userId);
        List<CartItem> items = cartItemService.findByCartId(cart.getId());
        return ResponseEntity.ok(Map.of("cartId", cart.getId(), "items", items));
    }

    /**
     * POST /api/carts/user/{userId}/add
     * Body: { productVariantId, quantity }
     * Adds a product variant to the cart (merges if already present).
     */
    @PostMapping("/user/{userId}/add")
    public ResponseEntity<?> addToCart(@PathVariable Long userId,
                                       @RequestBody Map<String, Object> body) {
        try {
            Long variantId = Long.valueOf(body.get("productVariantId").toString());
            int qty = Integer.parseInt(body.getOrDefault("quantity", "1").toString());

            ProductVariant variant = productVariantService.findById(variantId)
                    .orElseThrow(() -> new RuntimeException("Product variant not found: " + variantId));

            if (variant.getStock() < qty) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Not enough stock. Available: " + variant.getStock()));
            }

            Cart cart = getOrCreateCart(userId);

            // Check if variant already in cart
            Optional<CartItem> existing = cartItemService.findByCartAndVariant(cart.getId(), variantId);
            CartItem item;
            if (existing.isPresent()) {
                item = existing.get();
                int newQty = item.getQuantity() + qty;
                if (variant.getStock() < newQty) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "Not enough stock. Available: " + variant.getStock()));
                }
                item.setQuantity(newQty);
                item.setTotalPrice(variant.getPrice().multiply(BigDecimal.valueOf(newQty)));
                item = cartItemService.save(item);
            } else {
                item = new CartItem();
                item.setCart(cart);
                item.setProductVariant(variant);
                item.setQuantity(qty);
                item.setTotalPrice(variant.getPrice().multiply(BigDecimal.valueOf(qty)));
                item.setCreatedAt(LocalDateTime.now());
                item.setUpdatedAt(LocalDateTime.now());
                item.setIsDeleted(false);
                item = cartItemService.save(item);
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Added to cart",
                    "cartItemId", item.getId(),
                    "quantity", item.getQuantity(),
                    "totalPrice", item.getTotalPrice()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * PUT /api/carts/items/{itemId}/quantity
     * Body: { quantity }  — set a specific quantity (0 = remove)
     */
    @PutMapping("/items/{itemId}/quantity")
    public ResponseEntity<?> updateQuantity(@PathVariable Long itemId,
                                            @RequestBody Map<String, Object> body) {
        try {
            int qty = Integer.parseInt(body.get("quantity").toString());
            CartItem item = cartItemService.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Cart item not found"));

            if (qty <= 0) {
                cartItemService.delete(itemId);
                return ResponseEntity.ok(Map.of("message", "Item removed from cart"));
            }

            ProductVariant variant = item.getProductVariant();
            if (variant.getStock() < qty) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Not enough stock. Available: " + variant.getStock()));
            }
            item.setQuantity(qty);
            item.setTotalPrice(variant.getPrice().multiply(BigDecimal.valueOf(qty)));
            cartItemService.save(item);
            return ResponseEntity.ok(Map.of("message", "Quantity updated", "quantity", qty));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DELETE /api/carts/items/{itemId}
     */
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable Long itemId) {
        try {
            cartItemService.delete(itemId);
            return ResponseEntity.ok(Map.of("message", "Item removed"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DELETE /api/carts/user/{userId}/clear
     */
    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<?> clearCart(@PathVariable Long userId) {
        try {
            Cart cart = cartService.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));
            List<CartItem> items = cartItemService.findByCartId(cart.getId());
            items.forEach(item -> cartItemService.delete(item.getId()));
            return ResponseEntity.ok(Map.of("message", "Cart cleared", "removedItems", items.size()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Cart getOrCreateCart(Long userId) {
        return cartService.findByUserId(userId).orElseGet(() -> {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setCreatedAt(LocalDateTime.now());
            cart.setUpdatedAt(LocalDateTime.now());
            cart.setIsDeleted(false);
            return cartService.save(cart);
        });
    }
}
