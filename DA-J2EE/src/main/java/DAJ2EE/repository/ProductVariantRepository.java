package DAJ2EE.repository;

import DAJ2EE.entity.ProductVariant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    @EntityGraph(attributePaths = {"product", "product.category", "color", "size"})
    List<ProductVariant> findByIsDeletedFalse();

    @EntityGraph(attributePaths = {"product", "product.category", "color", "size"})
    List<ProductVariant> findByProductIdAndIsDeletedFalse(Long productId);
}
