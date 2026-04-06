package DAJ2EE.Service;

import DAJ2EE.entity.Role;
import DAJ2EE.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findByIsDeletedFalse();
    }

    public Optional<Role> findById(Long id) {
        return roleRepository.findByIdAndIsDeletedFalse(id);
    }

    public Optional<Role> findByCode(String code) {
        return roleRepository.findByCodeAndIsDeletedFalse(code);
    }

    public Role save(Role role) {
        if (role.getCreatedAt() == null) {
            role.setCreatedAt(LocalDateTime.now());
        }
        role.setUpdatedAt(LocalDateTime.now());
        role.setIsDeleted(false);
        return roleRepository.save(role);
    }

    public Role update(Long id, Role roleDetails) {
        Role role = findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        role.setName(roleDetails.getName());
        role.setCode(roleDetails.getCode());
        role.setUpdatedAt(LocalDateTime.now());
        return roleRepository.save(role);
    }

    public void delete(Long id) {
        Role role = findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        role.setIsDeleted(true);
        role.setDeletedAt(LocalDateTime.now());
        roleRepository.save(role);
    }
}
