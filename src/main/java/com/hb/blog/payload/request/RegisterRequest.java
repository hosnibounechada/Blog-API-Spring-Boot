package com.hb.blog.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class RegisterRequest extends LoginRequest {
    @JsonProperty("first_name")
    @NotBlank(message = "this field should not be blanc")
    @Size(min = 3, max = 50, message = "the size must be between 3 and 50")
    private String firstName;

    @NotBlank(message = "this field should not be blanc")
    @Size(min = 3, max = 50, message = "the size must be between 3 and 50")
    @JsonProperty("last_name")
    private String lastName;

    private Integer age;

    public RegisterRequest() {
    }

    public RegisterRequest(String email, String password, String firstName, String lastName, Integer age) {
        super(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RegisterRequest that = (RegisterRequest) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, age);
    }
}
