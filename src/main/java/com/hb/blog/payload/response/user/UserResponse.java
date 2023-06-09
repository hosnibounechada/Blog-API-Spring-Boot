package com.hb.blog.payload.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.hb.blog.util.StringUtils.*;

public record UserResponse(Long id,
                           @JsonProperty("first_name")
                           String firstName,
                           @JsonProperty("last_name") String lastName,
                           @JsonProperty("full_name") String fullName,
                           Integer age,
                           String email) {
    @Override
    public String firstName() {
        return upperCaseFirstLetter(firstName);
    }

    @Override
    public String lastName() {
        return upperCaseFirstLetter(lastName);
    }

    @Override
    public String fullName() {
        return firstName()+" "+lastName();
    }
}
