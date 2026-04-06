package DAJ2EE.repository;

import DAJ2EE.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByIsDeletedFalse();
    Optional<Role> findByIdAndIsDeletedFalse(Long id);
    Optional<Role> findByCodeAndIsDeletedFalse(String code);
}
