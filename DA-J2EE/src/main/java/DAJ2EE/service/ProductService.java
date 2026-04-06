package DAJ2EE.Service;

import DAJ2EE.entity.Product;
import DAJ2EE.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findByIsDeletedFalse();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id).filter(p -> p.getIsDeleted() == null || !p.getIsDeleted());
    }

    public Product save(Product product) {
        if (product.getCreatedAt() == null) {
            product.setCreatedAt(LocalDateTime.now());
        }
        product.setUpdatedAt(LocalDateTime.now());
        product.setIsDeleted(false);
        return productRepository.save(product);
    }

    public Product update(Long id, Product productDetails) {
        Product product = findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setImageUrl(productDetails.getImageUrl());
        product.setCategory(productDetails.getCategory());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public void delete(Long id) {
        Product product = findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setIsDeleted(true);
        product.setDeletedAt(LocalDateTime.now());
        
        // Cascading soft delete for variants
        if (product.getVariants() != null) {
            product.getVariants().forEach(variant -> {
                variant.setIsDeleted(true);
                variant.setDeletedAt(LocalDateTime.now());
            });
        }
        
        productRepository.save(product);
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(name);
    }

    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndIsDeletedFalse(categoryId);
    }
}
