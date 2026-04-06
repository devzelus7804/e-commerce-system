package DAJ2EE.dtos.Auth;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Setter
@Getter
public class LoginResponseDto {
    private String token;
    private ResponseDto user;
}
