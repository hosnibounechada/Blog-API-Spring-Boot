package com.hb.blog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class UserDTO {
    private Long id;

    @JsonProperty("first_name")
    @NotBlank(message = "this field should not be blanc")
    @Size(min = 3, max = 50, message = "the size must be between 3 and 50")
    private String firstName;

    @NotBlank(message = "this field should not be blanc")
    @Size(min = 3, max = 50, message = "the size must be between 3 and 50")
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("full_name")
    private String fullName;

    private Integer age;

    @NotBlank
    @Size(max = 50)
    @Email(message = "must be a syntactically correct email address")
    private String email;

    @NotBlank
    @Size(min = 6, max = 128)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public UserDTO() {
    }
    public UserDTO(String firstName, String lastName, Integer age, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
    }
    public UserDTO(Long id, String firstName, String lastName, Integer age, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    public UserDTO(Long id, String firstName, String lastName, String fullName, Integer age, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", lastName='" + fullName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(firstName, userDTO.firstName) && Objects.equals(lastName, userDTO.lastName) && Objects.equals(age, userDTO.age) && Objects.equals(email, userDTO.email) && Objects.equals(password, userDTO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, age, email, password);
    }
}
