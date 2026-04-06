package DAJ2EE.Controller.Admin;

import DAJ2EE.entity.ProductVariant;
import DAJ2EE.Service.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/product-variants")
public class AdminProductVariantController {

    @Autowired
    private ProductVariantService productVariantService;

    @GetMapping
    public List<ProductVariant> getAll() {
        return productVariantService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductVariant> getById(@PathVariable Long id) {
        return productVariantService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProductVariant create(@RequestBody ProductVariant productVariant) {
        return productVariantService.save(productVariant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductVariant> update(@PathVariable Long id, @RequestBody ProductVariant variantDetails) {
        try {
            return ResponseEntity.ok(productVariantService.update(id, variantDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            productVariantService.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
