package DAJ2EE.repository;

import DAJ2EE.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"category"})
    List<Product> findByIsDeletedFalse();

    @EntityGraph(attributePaths = {"category"})
    List<Product> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name);

    @EntityGraph(attributePaths = {"category"})
    List<Product> findByCategoryIdAndIsDeletedFalse(Long categoryId);
}
