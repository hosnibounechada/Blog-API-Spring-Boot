package com.hb.blog.payload.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(@JsonProperty("first_name")
                                @NotBlank(message = "this field should not be blanc")
                                @Size(min = 3, max = 50, message = "the size must be between 3 and 50")
                                String firstName,
                                @NotBlank(message = "this field should not be blanc")
                                @Size(min = 3, max = 50, message = "the size must be between 3 and 50")
                                @JsonProperty("last_name")
                                String lastName,
                                Integer age,
                                @NotBlank
                                @Size(max = 50)
                                @Email(message = "must be a syntactically correct email address")
                                String email,
                                @NotBlank
                                @Size(min = 6, max = 128)
                                @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
                                String password) {
}
