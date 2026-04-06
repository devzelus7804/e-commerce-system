package DAJ2EE.Service;

import DAJ2EE.entity.Category;
import DAJ2EE.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findByIsDeletedFalse();
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id).filter(c -> c.getIsDeleted() == null || !c.getIsDeleted());
    }

    public Category save(Category category) {
        if (category.getCreatedAt() == null) {
            category.setCreatedAt(LocalDateTime.now());
        }
        category.setUpdatedAt(LocalDateTime.now());
        category.setIsDeleted(false);
        return categoryRepository.save(category);
    }

    public Category update(Long id, Category categoryDetails) {
        Category category = findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(categoryDetails.getName());
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        Category category = findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setIsDeleted(true);
        category.setDeletedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }
}
