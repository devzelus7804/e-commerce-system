package DAJ2EE.controller.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import DAJ2EE.dtos.Auth.LoginDto;
import DAJ2EE.dtos.Auth.LoginResponseDto;
import DAJ2EE.dtos.Auth.RegisterDto;
import DAJ2EE.service.Auth.AuthService;

@RestController
@RequestMapping("auth")
public class AuthController {
    private AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public String register(@RequestBody RegisterDto registerDto) {
        return authService.register(registerDto);
    }
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }
}
