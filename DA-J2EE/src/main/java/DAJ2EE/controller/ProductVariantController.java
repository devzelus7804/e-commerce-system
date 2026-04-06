package DAJ2EE.Controller;

import DAJ2EE.Service.ProductVariantService;
import DAJ2EE.entity.ProductVariant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-variants")
public class ProductVariantController {

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

    @GetMapping("/product/{productId}")
    public List<ProductVariant> getByProduct(@PathVariable Long productId) {
        return productVariantService.findByProduct(productId);
    }
}
