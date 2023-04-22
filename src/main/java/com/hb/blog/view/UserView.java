package com.hb.blog.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.hb.blog.util.StringUtils.upperCaseFirstLetter;

public interface UserView {
    public Long getId();
    @JsonProperty("first_name")
    String getFirstName();
    @JsonProperty("last_name")
    String getLastName();
    public Integer getAge();
    @JsonProperty("full_name")
    default String getFullName(){
        return upperCaseFirstLetter(getFirstName()) +" "+upperCaseFirstLetter(getLastName());
    }
}
