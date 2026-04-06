package DAJ2EE.repository;

<<<<<<< HEAD
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import DAJ2EE.entity.UserAddress;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
=======
import DAJ2EE.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findByIsDeletedFalse();
    Optional<UserAddress> findByIdAndIsDeletedFalse(Long id);
    List<UserAddress> findByUserIdAndIsDeletedFalse(Long userId);
>>>>>>> a29c2501f546ed0110a8790d1ec5f04b8558b864
    List<UserAddress> findByUserId(Long userId);
}
