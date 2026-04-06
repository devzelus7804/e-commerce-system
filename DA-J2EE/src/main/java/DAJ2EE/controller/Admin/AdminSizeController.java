package DAJ2EE.Controller.Admin;

import DAJ2EE.Service.SizeService;
import DAJ2EE.entity.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/sizes")
public class AdminSizeController {

    @Autowired
    private SizeService sizeService;

    @GetMapping
    public List<Size> getAll() {
        return sizeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Size> getById(@PathVariable Long id) {
        return sizeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Size create(@RequestBody Size size) {
        return sizeService.save(size);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Size> update(@PathVariable Long id, @RequestBody Size sizeDetails) {
        try {
            return ResponseEntity.ok(sizeService.update(id, sizeDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            sizeService.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
