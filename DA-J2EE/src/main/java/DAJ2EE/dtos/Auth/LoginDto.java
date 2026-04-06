package DAJ2EE.dtos.Auth;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Setter
@Getter
public class LoginDto {
    private String username;
    private String password;
}
