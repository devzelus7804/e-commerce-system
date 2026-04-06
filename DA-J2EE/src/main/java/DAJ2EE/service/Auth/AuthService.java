package DAJ2EE.service.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import DAJ2EE.dtos.Auth.LoginDto;
import DAJ2EE.dtos.Auth.LoginResponseDto;
import DAJ2EE.dtos.Auth.RegisterDto;
import DAJ2EE.dtos.Auth.ResponseDto;
import DAJ2EE.entity.User;
import DAJ2EE.enums.RoleEnum;
import DAJ2EE.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String register(RegisterDto registerDto) {
        if(userRepository.existsByUsername(registerDto.getUsername())) {
            return "Username already exists";
        }
        if(userRepository.existsByEmail(registerDto.getEmail())) {
            return "Email already exists";
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setEmail(registerDto.getEmail());
        user.setFullName(registerDto.getFullName());
        user.setGender(registerDto.getGender());
        user.setPhone(registerDto.getPhone());
        user.setRole(RoleEnum.USER);

        userRepository.save(user);
        return "Register success";
    }

    public LoginResponseDto login(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }

        String token = jwtService.generateToken(user);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setId(user.getId());
        responseDto.setFullName(user.getFullName());
        responseDto.setIsActive(user.getIsActive());
        responseDto.setCreatedAt(user.getCreatedAt());

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setToken(token);
        loginResponseDto.setUser(responseDto);

        return loginResponseDto;
    }
}
