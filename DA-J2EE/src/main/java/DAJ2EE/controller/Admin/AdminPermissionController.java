package DAJ2EE.Controller.Admin;

import DAJ2EE.entity.Permission;
import DAJ2EE.Service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/permissions")
public class AdminPermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public List<Permission> getAll() {
        return permissionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permission> getById(@PathVariable Long id) {
        return permissionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Permission create(@RequestBody Permission permission) {
        return permissionService.save(permission);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Permission> update(@PathVariable Long id, @RequestBody Permission permissionDetails) {
        try {
            return ResponseEntity.ok(permissionService.update(id, permissionDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            permissionService.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
