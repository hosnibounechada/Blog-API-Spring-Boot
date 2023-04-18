package com.hb.blog.payload.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserResponse(Long id,
                           @JsonProperty("first_name")
                           String firstName,
                           @JsonProperty("last_name") String lastName,
                           @JsonProperty("full_name") String fullName,
                           Integer age,
                           String email) {
}
