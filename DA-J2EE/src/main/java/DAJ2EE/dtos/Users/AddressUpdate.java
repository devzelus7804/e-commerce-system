package DAJ2EE.dtos.Users;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AddressUpdate {
    // --- Input fields (client gửi lên, tất cả optional vì là partial update) ---
    private String address;
    private String city;
    private String state;
    private String country;

    // --- Output fields (server trả về) ---
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
