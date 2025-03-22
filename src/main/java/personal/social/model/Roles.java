package personal.social.model;

import jakarta.persistence.*;
import personal.social.enums.RoleEnum;

@Entity
public class Roles {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Roles(Integer roleId, RoleEnum role, User user) {
        this.roleId = roleId;
        this.role = role;
        this.user = user;
    }

    public Roles() {
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
