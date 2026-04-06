package DAJ2EE.Controller.Admin;

import DAJ2EE.entity.UserAddress;
import DAJ2EE.Service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user-addresses")
public class AdminUserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @GetMapping
    public List<UserAddress> getAll() {
        return userAddressService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAddress> getById(@PathVariable Long id) {
        return userAddressService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public UserAddress create(@RequestBody UserAddress userAddress) {
        return userAddressService.save(userAddress);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAddress> update(@PathVariable Long id, @RequestBody UserAddress addressDetails) {
        try {
            return ResponseEntity.ok(userAddressService.update(id, addressDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            userAddressService.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
