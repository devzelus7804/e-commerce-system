package DAJ2EE.dtos.Auth;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Setter
@Getter
public class ResponseDto {
    private long id;
    private String fullName;
    private Boolean isActive;
    private LocalDateTime createdAt;

}
