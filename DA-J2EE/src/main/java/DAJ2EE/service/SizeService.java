package DAJ2EE.Service;

import DAJ2EE.entity.Size;
import DAJ2EE.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    public List<Size> findAll() {
        return sizeRepository.findByIsDeletedFalse();
    }

    public Optional<Size> findById(Long id) {
        return sizeRepository.findById(id).filter(s -> s.getIsDeleted() == null || !s.getIsDeleted());
    }

    public Size save(Size size) {
        if (size.getCreatedAt() == null) {
            size.setCreatedAt(LocalDateTime.now());
        }
        size.setUpdatedAt(LocalDateTime.now());
        size.setIsDeleted(false);
        return sizeRepository.save(size);
    }

    public Size update(Long id, Size sizeDetails) {
        Size size = findById(id).orElseThrow(() -> new RuntimeException("Size not found"));
        size.setName(sizeDetails.getName());
        size.setUpdatedAt(LocalDateTime.now());
        return sizeRepository.save(size);
    }

    public void delete(Long id) {
        Size size = findById(id).orElseThrow(() -> new RuntimeException("Size not found"));
        size.setIsDeleted(true);
        size.setDeletedAt(LocalDateTime.now());
        sizeRepository.save(size);
    }
}
