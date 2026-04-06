package DAJ2EE.entity;

<<<<<<< HEAD
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import DAJ2EE.enums.GenderEnum;
import DAJ2EE.enums.RoleEnum;
import jakarta.persistence.*;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Setter
@Getter
@Entity(name = "users")
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Cần phải có tên đăng nhập")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải từ 3 đến 50 ký tự")
    @Column(nullable = false, unique = true)
    private String username;

    @Email(message = "Email không hợp lệ")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Cần phải có mật khẩu")
    @Size(min = 6, message = "Mật khẩu phải ít nhất 6 ký tự")
    @Column(nullable = false)
    private String password;

    @Size(max = 100, message = "Họ và tên không được vượt quá 100 ký tự")
    private String fullName;
    private String phone;
    private GenderEnum gender;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role;

    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
=======
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(length = 20)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = true)
    private Role role;
>>>>>>> a29c2501f546ed0110a8790d1ec5f04b8558b864
}
