package DAJ2EE.Service;

import DAJ2EE.entity.Permission;
import DAJ2EE.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Permission> findAll() {
        return permissionRepository.findByIsDeletedFalse();
    }

    public Optional<Permission> findById(Long id) {
        return permissionRepository.findByIdAndIsDeletedFalse(id);
    }

    public Optional<Permission> findByCode(String code) {
        return permissionRepository.findByCodeAndIsDeletedFalse(code);
    }

    public Permission save(Permission permission) {
        if (permission.getCreatedAt() == null) {
            permission.setCreatedAt(LocalDateTime.now());
        }
        permission.setUpdatedAt(LocalDateTime.now());
        permission.setIsDeleted(false);
        return permissionRepository.save(permission);
    }

    public Permission update(Long id, Permission permissionDetails) {
        Permission permission = findById(id).orElseThrow(() -> new RuntimeException("Permission not found"));
        permission.setName(permissionDetails.getName());
        permission.setCode(permissionDetails.getCode());
        permission.setUpdatedAt(LocalDateTime.now());
        return permissionRepository.save(permission);
    }

    public void delete(Long id) {
        Permission permission = findById(id).orElseThrow(() -> new RuntimeException("Permission not found"));
        permission.setIsDeleted(true);
        permission.setDeletedAt(LocalDateTime.now());
        permissionRepository.save(permission);
    }
}
