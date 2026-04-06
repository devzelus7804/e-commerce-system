package DAJ2EE.dtos.Users;

import DAJ2EE.enums.GenderEnum;
import DAJ2EE.enums.RoleEnum;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private GenderEnum gender;
    private RoleEnum role;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
