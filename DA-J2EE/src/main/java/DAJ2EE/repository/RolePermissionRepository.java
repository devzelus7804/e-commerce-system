package DAJ2EE.repository;

import DAJ2EE.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByIsDeletedFalse();
    Optional<RolePermission> findByIdAndIsDeletedFalse(Long id);
    List<RolePermission> findByRoleIdAndIsDeletedFalse(Long roleId);
    List<RolePermission> findByPermissionIdAndIsDeletedFalse(Long permissionId);
    Optional<RolePermission> findByRoleIdAndPermissionIdAndIsDeletedFalse(Long roleId, Long permissionId);
}
