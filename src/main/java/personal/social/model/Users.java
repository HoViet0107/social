package personal.social.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(columnDefinition = "VARCHAR(155)", unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String firstName;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String surname;

    @Column(name = "last_name", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String lastName;

    @Column(columnDefinition = "VARCHAR(12)", unique = true)
    private String phone;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles roles;
}
