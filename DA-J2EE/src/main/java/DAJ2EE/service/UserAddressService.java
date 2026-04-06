package DAJ2EE.Service;

import DAJ2EE.entity.UserAddress;
import DAJ2EE.repository.UserAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserAddressService {

    @Autowired
    private UserAddressRepository userAddressRepository;

    public List<UserAddress> findAll() {
        return userAddressRepository.findByIsDeletedFalse();
    }

    public Optional<UserAddress> findById(Long id) {
        return userAddressRepository.findByIdAndIsDeletedFalse(id);
    }

    public List<UserAddress> findByUserId(Long userId) {
        return userAddressRepository.findByUserIdAndIsDeletedFalse(userId);
    }

    public UserAddress save(UserAddress userAddress) {
        if (userAddress.getCreatedAt() == null) {
            userAddress.setCreatedAt(LocalDateTime.now());
        }
        userAddress.setUpdatedAt(LocalDateTime.now());
        userAddress.setIsDeleted(false);
        return userAddressRepository.save(userAddress);
    }

    public UserAddress update(Long id, UserAddress addressDetails) {
        UserAddress address = findById(id).orElseThrow(() -> new RuntimeException("UserAddress not found"));
        address.setUser(addressDetails.getUser());
        address.setReceiverName(addressDetails.getReceiverName());
        address.setPhone(addressDetails.getPhone());
        address.setAddress(addressDetails.getAddress());
        address.setCity(addressDetails.getCity());
        address.setDistrict(addressDetails.getDistrict());
        address.setIsDefault(addressDetails.getIsDefault());
        address.setUpdatedAt(LocalDateTime.now());
        return userAddressRepository.save(address);
    }

    public void delete(Long id) {
        UserAddress address = findById(id).orElseThrow(() -> new RuntimeException("UserAddress not found"));
        address.setIsDeleted(true);
        address.setDeletedAt(LocalDateTime.now());
        userAddressRepository.save(address);
    }
}
