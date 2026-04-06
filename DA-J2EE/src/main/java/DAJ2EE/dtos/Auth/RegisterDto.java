package DAJ2EE.dtos.Auth;


import DAJ2EE.enums.GenderEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
public class RegisterDto {
    private String username;
    private String email;
    private String password;
    private String fullName;
    private GenderEnum gender;
    private String phone;
}
