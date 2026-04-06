package DAJ2EE.controller.Users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import DAJ2EE.dtos.Users.AddressCreateDto;
import DAJ2EE.dtos.Users.AddressUpdate;
import DAJ2EE.dtos.Users.UserAddressDto;
import DAJ2EE.dtos.Users.UserUpdateDto;
import DAJ2EE.service.Users.UserService;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("users")
public class UserController {
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public UserUpdateDto updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto) {
        return userService.updateUser(id, userUpdateDto);
    }
    @PostMapping("/address")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressCreateDto> addAddress(Authentication authentication, @Valid @RequestBody AddressCreateDto addressCreateDto) {
        AddressCreateDto result = userService.addAddress(authentication, addressCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
    @PutMapping("/address/{addressId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressUpdate> updateAddress(Authentication authentication, @PathVariable Long addressId, @RequestBody AddressUpdate addressUpdate) {
        AddressUpdate result = userService.updateAddress(authentication, addressId, addressUpdate);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/address")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UserAddressDto>> getAllAddresses(Authentication authentication) {
        List<UserAddressDto> addresses = userService.getAllAddresses(authentication);
        return ResponseEntity.ok(addresses);
    }
    @DeleteMapping("/address/{addressId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteAddress(Authentication authentication, @PathVariable Long addressId) {
        userService.deleteAddress(authentication, addressId);
        return ResponseEntity.noContent().build();
    }
}
