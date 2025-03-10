package personal.social.model;

import jakarta.persistence.*;
import lombok.Data;
import personal.social.enums.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column(columnDefinition = "VARCHAR(155)", unique = true)
    private String email;

    @Column(columnDefinition = "VARCHAR(16)" , nullable = false)
    private String password;

    @Column(nullable = false)
    private String first_name;

    private String surname;

    @Column(nullable = false)
    private String last_name;

    @Column(columnDefinition = "VARCHAR(12)", unique = true)
    private String phone;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime created_at;

    @Enumerated(EnumType.STRING)
    private Role role;
}
