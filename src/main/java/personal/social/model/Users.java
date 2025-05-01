package personal.social.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class User {
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

    public User() {
    }

    public User(Long userId, String email, String password, String firstName, String surname, String lastName, String phone, LocalDate dob, LocalDateTime createdAt) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.lastName = lastName;
        this.phone = phone;
        this.dob = dob;
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
