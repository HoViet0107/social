package personal.social.model;

import jakarta.persistence.*;
import lombok.Data;
import personal.social.enums.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column(columnDefinition = "VARCHAR(155)", unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "NVARCHAR(255)", nullable = false)
    private String first_name;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String surname;

    @Column(columnDefinition = "NVARCHAR(255)", nullable = false)
    private String last_name;

    @Column(columnDefinition = "VARCHAR(12)", unique = true)
    private String phone;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime created_at;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {
    }

    public User(Integer user_id, String email, String password, String first_name, String surname, String last_name, String phone, LocalDate dob, LocalDateTime created_at, Role role) {
        this.user_id = user_id;
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.surname = surname;
        this.last_name = last_name;
        this.phone = phone;
        this.dob = dob;
        this.created_at = created_at;
        this.role = role;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
