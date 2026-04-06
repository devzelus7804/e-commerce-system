package DAJ2EE.Controller;

import DAJ2EE.entity.User;
import DAJ2EE.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User newUser = userService.register(user);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Đăng ký thành công");
            response.put("user", newUser);
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            String message = "Tên đăng nhập hoặc email đã tồn tại";
            return ResponseEntity.badRequest().body(Map.of("error", message));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpSession session) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Optional<User> user = userService.authenticate(username, password);
        if (user.isPresent()) {
            // Store user in session for authorization checks
            session.setAttribute("currentUser", user.get());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("user", user.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid username or password"));
        }
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable Long userId) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
