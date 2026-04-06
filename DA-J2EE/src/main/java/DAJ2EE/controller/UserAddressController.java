package DAJ2EE.Controller;

import DAJ2EE.entity.UserAddress;
import DAJ2EE.Service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-addresses")
public class UserAddressController {

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

    @GetMapping("/user/{userId}")
    public List<UserAddress> getByUserId(@PathVariable Long userId) {
        return userAddressService.findByUserId(userId);
    }
}
