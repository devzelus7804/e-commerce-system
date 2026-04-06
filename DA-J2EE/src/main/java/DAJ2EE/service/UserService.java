package DAJ2EE.Service;

import DAJ2EE.entity.Role;
import DAJ2EE.entity.User;
import DAJ2EE.repository.CartItemRepository;
import DAJ2EE.repository.CartRepository;
import DAJ2EE.repository.UserAddressRepository;
import DAJ2EE.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    public List<User> findAll() {
        return userRepository.findByIsDeletedFalse();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findByIdAndIsDeletedFalse(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailAndIsDeletedFalse(email);
    }

    public User register(User user) {
        // Validate username/email not exist
        if (userRepository.findByUsernameAndIsDeletedFalse(user.getUsername()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        if (userRepository.findByEmailAndIsDeletedFalse(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // Assign default role (USER) if exists; do not create it here
        Role assignedRole = roleService.findByCode("USER")
                .orElseThrow(() -> new RuntimeException("Role USER chưa được cấu hình. Vui lòng thêm role USER trước."));
        user.setRole(assignedRole);

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsDeleted(false);

        User savedUser = userRepository.save(user);

        // Auto-create cart for new user
        DAJ2EE.entity.Cart cart = new DAJ2EE.entity.Cart();
        cart.setUser(savedUser);
        cartService.save(cart);

        return savedUser;
    }

    public Optional<User> authenticate(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndIsDeletedFalse(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }

    public User update(Long id, User userDetails) {
        User user = findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if (userDetails.getUsername() != null && !userDetails.getUsername().equals(user.getUsername())) {
            if (userRepository.findByUsernameAndIsDeletedFalse(userDetails.getUsername()).isPresent()) {
                throw new RuntimeException("Tên đăng nhập đã tồn tại");
            }
            user.setUsername(userDetails.getUsername());
        }

        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmailAndIsDeletedFalse(userDetails.getEmail()).isPresent()) {
                throw new RuntimeException("Email đã tồn tại");
            }
            user.setEmail(userDetails.getEmail());
        }

        if (userDetails.getFullName() != null) {
            user.setFullName(userDetails.getFullName());
        }

        if (userDetails.getPhone() != null) {
            user.setPhone(userDetails.getPhone());
        }

        if (userDetails.getPassword() != null) {
            user.setPassword(userDetails.getPassword());
        }

        if (userDetails.getRole() != null && userDetails.getRole().getCode() != null) {
            roleService.findByCode(userDetails.getRole().getCode())
                    .ifPresent(user::setRole);
        }

        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void delete(Long id) {
        User user = findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void hardDelete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Delete addresses
        userAddressRepository.deleteAll(userAddressRepository.findByUserId(id));
        // Delete cart items then carts
        cartRepository.findByUserId(id).forEach(cart -> {
            cartItemRepository.deleteAll(cartItemRepository.findByCartId(cart.getId()));
            cartRepository.delete(cart);
        });
        userRepository.delete(user);
    }
}
