package DAJ2EE.Service;

import DAJ2EE.entity.Color;
import DAJ2EE.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ColorService {

    @Autowired
    private ColorRepository colorRepository;

    public List<Color> findAll() {
        return colorRepository.findByIsDeletedFalse();
    }

    public Optional<Color> findById(Long id) {
        return colorRepository.findById(id).filter(c -> c.getIsDeleted() == null || !c.getIsDeleted());
    }

    public Color save(Color color) {
        if (color.getCreatedAt() == null) {
            color.setCreatedAt(LocalDateTime.now());
        }
        color.setUpdatedAt(LocalDateTime.now());
        color.setIsDeleted(false);
        return colorRepository.save(color);
    }

    public Color update(Long id, Color colorDetails) {
        Color color = findById(id).orElseThrow(() -> new RuntimeException("Color not found"));
        color.setName(colorDetails.getName());
        color.setHexCode(colorDetails.getHexCode());
        color.setUpdatedAt(LocalDateTime.now());
        return colorRepository.save(color);
    }

    public void delete(Long id) {
        Color color = findById(id).orElseThrow(() -> new RuntimeException("Color not found"));
        color.setIsDeleted(true);
        color.setDeletedAt(LocalDateTime.now());
        colorRepository.save(color);
    }
}
