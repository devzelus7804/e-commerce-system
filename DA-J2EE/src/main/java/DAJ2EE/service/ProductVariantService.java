package DAJ2EE.Service;

import DAJ2EE.entity.ProductVariant;
import DAJ2EE.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductVariantService {

    @Autowired
    private ProductVariantRepository productVariantRepository;

    public List<ProductVariant> findAll() {
        return productVariantRepository.findByIsDeletedFalse();
    }

    public Optional<ProductVariant> findById(Long id) {
        return productVariantRepository.findById(id).filter(pv -> pv.getIsDeleted() == null || !pv.getIsDeleted());
    }

    public ProductVariant save(ProductVariant productVariant) {
        if (productVariant.getCreatedAt() == null) {
            productVariant.setCreatedAt(LocalDateTime.now());
        }
        productVariant.setUpdatedAt(LocalDateTime.now());
        productVariant.setIsDeleted(false);
        return productVariantRepository.save(productVariant);
    }

    public ProductVariant update(Long id, ProductVariant variantDetails) {
        ProductVariant variant = findById(id).orElseThrow(() -> new RuntimeException("ProductVariant not found"));
        variant.setProduct(variantDetails.getProduct());
        variant.setName(variantDetails.getName());
        variant.setColor(variantDetails.getColor());
        variant.setSize(variantDetails.getSize());
        variant.setPrice(variantDetails.getPrice());
        variant.setStock(variantDetails.getStock());
        variant.setSku(variantDetails.getSku());
        variant.setUpdatedAt(LocalDateTime.now());
        return productVariantRepository.save(variant);
    }

    public void delete(Long id) {
        ProductVariant variant = findById(id).orElseThrow(() -> new RuntimeException("ProductVariant not found"));
        variant.setIsDeleted(true);
        variant.setDeletedAt(LocalDateTime.now());
        productVariantRepository.save(variant);
    }

    public List<ProductVariant> findByProduct(Long productId) {
        return productVariantRepository.findByProductIdAndIsDeletedFalse(productId);
    }
}
