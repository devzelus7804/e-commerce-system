package DAJ2EE.service.Users;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import DAJ2EE.dtos.Users.AddressCreateDto;
import DAJ2EE.dtos.Users.AddressUpdate;
import DAJ2EE.dtos.Users.UserAddressDto;
import DAJ2EE.dtos.Users.UserUpdateDto;
import DAJ2EE.entity.User;
import DAJ2EE.entity.UserAddress;
import DAJ2EE.repository.UserAddressRepository;
import DAJ2EE.repository.UserRepository;
import DAJ2EE.security.CustomUserPrincipal;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    public UserUpdateDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (userUpdateDto.getFullName() != null) {
            user.setFullName(userUpdateDto.getFullName());
        }
        if (userUpdateDto.getPhone() != null) {
            user.setPhone(userUpdateDto.getPhone());
        }
        if (userUpdateDto.getGender() != null) {
            user.setGender(userUpdateDto.getGender());
        }
        if (userUpdateDto.getUpdatedAt() != null) {
            user.setUpdatedAt(userUpdateDto.getUpdatedAt());
        }
        userRepository.save(user);
        userUpdateDto.setFullName(user.getFullName());
        userUpdateDto.setPhone(user.getPhone());
        userUpdateDto.setGender(user.getGender());
        userUpdateDto.setUpdatedAt(user.getUpdatedAt());
        return userUpdateDto;
    }

    public AddressCreateDto addAddress(Authentication authentication, AddressCreateDto addressCreateDto) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(user.getId());
        userAddress.setAddress(addressCreateDto.getAddress());
        userAddress.setCity(addressCreateDto.getCity());
        userAddress.setState(addressCreateDto.getState());
        userAddress.setCountry(addressCreateDto.getCountry());

        UserAddress savedAddress = userAddressRepository.save(userAddress);

        addressCreateDto.setUserId(savedAddress.getUserId());
        addressCreateDto.setAddress(savedAddress.getAddress());
        addressCreateDto.setCity(savedAddress.getCity());
        addressCreateDto.setState(savedAddress.getState());
        addressCreateDto.setCountry(savedAddress.getCountry());
        addressCreateDto.setCreatedAt(savedAddress.getCreatedAt());
        addressCreateDto.setUpdatedAt(savedAddress.getUpdatedAt());
        return addressCreateDto;
    }

    public AddressUpdate updateAddress(Authentication authentication, Long addressId, AddressUpdate addressUpdate) {
        // Lấy userId từ JWT
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();

        // Tìm address theo id
        UserAddress userAddress = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // Kiểm tra quyền sở hữu: address này có thuộc user đang đăng nhập không
        if (!userAddress.getUserId().equals(userId)) {
            throw new RuntimeException("You do not have permission to update this address");
        }

        // Partial update — chỉ update field nào client gửi lên
        if (addressUpdate.getAddress() != null) {
            userAddress.setAddress(addressUpdate.getAddress());
        }
        if (addressUpdate.getCity() != null) {
            userAddress.setCity(addressUpdate.getCity());
        }
        if (addressUpdate.getState() != null) {
            userAddress.setState(addressUpdate.getState());
        }
        if (addressUpdate.getCountry() != null) {
            userAddress.setCountry(addressUpdate.getCountry());
        }

        UserAddress savedAddress = userAddressRepository.save(userAddress);

        // Trả về response
        addressUpdate.setId(savedAddress.getId());
        addressUpdate.setUserId(savedAddress.getUserId());
        addressUpdate.setAddress(savedAddress.getAddress());
        addressUpdate.setCity(savedAddress.getCity());
        addressUpdate.setState(savedAddress.getState());
        addressUpdate.setCountry(savedAddress.getCountry());
        addressUpdate.setCreatedAt(savedAddress.getCreatedAt());
        addressUpdate.setUpdatedAt(savedAddress.getUpdatedAt());
        return addressUpdate;
    }

    public List<UserAddressDto> getAllAddresses(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();

        List<UserAddress> addresses = userAddressRepository.findByUserId(userId);

        return addresses.stream().map(address -> {
            UserAddressDto dto = new UserAddressDto();
            dto.setId(address.getId());
            dto.setAddress(address.getAddress());
            dto.setCity(address.getCity());
            dto.setState(address.getState());
            dto.setCountry(address.getCountry());
            dto.setCreatedAt(address.getCreatedAt());
            dto.setUpdatedAt(address.getUpdatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    public void deleteAddress(Authentication authentication, Long addressId) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();

        UserAddress userAddress = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!userAddress.getUserId().equals(userId)) {
            throw new RuntimeException("You do not have permission to delete this address");
        }

        userAddressRepository.delete(userAddress);
    }
}
