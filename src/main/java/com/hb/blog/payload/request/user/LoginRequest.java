package com.hb.blog.payload.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank
        @Size(max = 50)
        @Email(message = "must be a syntactically correct email address")
        String email,
        @NotBlank
        @Size(min = 6, max = 128)
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password) {
}
