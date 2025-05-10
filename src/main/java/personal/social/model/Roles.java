package personal.social.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import personal.social.enums.RoleEnum;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class Roles {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public Roles(RoleEnum role) {
        this.role = role;
    }
}
