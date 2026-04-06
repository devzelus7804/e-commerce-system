package DAJ2EE.Service;

import DAJ2EE.entity.RolePermission;
import DAJ2EE.repository.RolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RolePermissionService {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    public List<RolePermission> findAll() {
        return rolePermissionRepository.findByIsDeletedFalse();
    }

    public Optional<RolePermission> findById(Long id) {
        return rolePermissionRepository.findByIdAndIsDeletedFalse(id);
    }

    public List<RolePermission> findByRoleId(Long roleId) {
        return rolePermissionRepository.findByRoleIdAndIsDeletedFalse(roleId);
    }

    public List<RolePermission> findByPermissionId(Long permissionId) {
        return rolePermissionRepository.findByPermissionIdAndIsDeletedFalse(permissionId);
    }

    public Optional<RolePermission> findByRoleIdAndPermissionId(Long roleId, Long permissionId) {
        return rolePermissionRepository.findByRoleIdAndPermissionIdAndIsDeletedFalse(roleId, permissionId);
    }

    public RolePermission save(RolePermission rolePermission) {
        if (rolePermission.getCreatedAt() == null) {
            rolePermission.setCreatedAt(LocalDateTime.now());
        }
        rolePermission.setUpdatedAt(LocalDateTime.now());
        rolePermission.setIsDeleted(false);
        return rolePermissionRepository.save(rolePermission);
    }

    public RolePermission update(Long id, RolePermission rpDetails) {
        RolePermission rp = findById(id).orElseThrow(() -> new RuntimeException("RolePermission not found"));
        rp.setRole(rpDetails.getRole());
        rp.setPermission(rpDetails.getPermission());
        rp.setIsEnabled(rpDetails.getIsEnabled());
        rp.setUpdatedAt(LocalDateTime.now());
        return rolePermissionRepository.save(rp);
    }

    public void delete(Long id) {
        RolePermission rp = findById(id).orElseThrow(() -> new RuntimeException("RolePermission not found"));
        rp.setIsDeleted(true);
        rp.setDeletedAt(LocalDateTime.now());
        rolePermissionRepository.save(rp);
    }
}
