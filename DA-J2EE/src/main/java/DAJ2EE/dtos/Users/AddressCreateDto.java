package DAJ2EE.dtos.Users;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class AddressCreateDto {
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "City is required")
    private String city;
    @NotBlank(message = "State is required")
    private String state;
    @NotBlank(message = "Country is required")
    private String country;

    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
