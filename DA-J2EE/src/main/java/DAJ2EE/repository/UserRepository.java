package DAJ2EE.repository;

<<<<<<< HEAD
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import DAJ2EE.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
}

=======
import DAJ2EE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByIsDeletedFalse();
    Optional<User> findByIdAndIsDeletedFalse(Long id);
    Optional<User> findByUsernameAndIsDeletedFalse(String username);
    Optional<User> findByEmailAndIsDeletedFalse(String email);
}
>>>>>>> a29c2501f546ed0110a8790d1ec5f04b8558b864
