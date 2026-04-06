package DAJ2EE.Controller.Admin;

import DAJ2EE.entity.Color;
import DAJ2EE.Service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/colors")
public class AdminColorController {

    @Autowired
    private ColorService colorService;

    @GetMapping
    public List<Color> getAll() {
        return colorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Color> getById(@PathVariable Long id) {
        return colorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Color create(@RequestBody Color color) {
        return colorService.save(color);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Color> update(@PathVariable Long id, @RequestBody Color colorDetails) {
        try {
            return ResponseEntity.ok(colorService.update(id, colorDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            colorService.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
