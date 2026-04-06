package DAJ2EE.Controller.Admin;

import DAJ2EE.entity.RolePermission;
import DAJ2EE.Service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/role-permissions")
public class AdminRolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

    @GetMapping
    public List<RolePermission> getAll() {
        return rolePermissionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolePermission> getById(@PathVariable Long id) {
        return rolePermissionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/role/{roleId}")
    public List<RolePermission> getByRoleId(@PathVariable Long roleId) {
        return rolePermissionService.findByRoleId(roleId);
    }

    @GetMapping("/permission/{permissionId}")
    public List<RolePermission> getByPermissionId(@PathVariable Long permissionId) {
        return rolePermissionService.findByPermissionId(permissionId);
    }

    @PostMapping
    public RolePermission create(@RequestBody RolePermission rolePermission) {
        return rolePermissionService.save(rolePermission);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolePermission> update(@PathVariable Long id, @RequestBody RolePermission rpDetails) {
        try {
            return ResponseEntity.ok(rolePermissionService.update(id, rpDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            rolePermissionService.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
