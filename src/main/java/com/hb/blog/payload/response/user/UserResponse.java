package com.hb.blog.payload.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(Long id,
                           @JsonProperty("first_name")
                           String firstName,
                           @JsonProperty("last_name") String lastName,
                           @JsonProperty("full_name") String fullName,
                           Integer age,
                           String email) {
    @Override
    public String firstName() {
        return firstName.substring(0,1).toUpperCase()+firstName.substring(1);
    }

    @Override
    public String lastName() {
        return lastName.substring(0,1).toUpperCase()+lastName.substring(1);
    }

    @Override
    public String fullName() {
        return firstName()+" "+lastName();
    }
}
