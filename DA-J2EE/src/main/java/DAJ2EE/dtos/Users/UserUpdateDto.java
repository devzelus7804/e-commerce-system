package DAJ2EE.dtos.Users;

import java.time.LocalDateTime;
import DAJ2EE.enums.GenderEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserUpdateDto {
    private String fullName;
    private String phone;
    private GenderEnum gender;
    private LocalDateTime updatedAt;
}
