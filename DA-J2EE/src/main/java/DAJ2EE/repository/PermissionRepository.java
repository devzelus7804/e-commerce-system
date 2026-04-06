package DAJ2EE.repository;

import DAJ2EE.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findByIsDeletedFalse();
    Optional<Permission> findByIdAndIsDeletedFalse(Long id);
    Optional<Permission> findByCodeAndIsDeletedFalse(String code);
}
