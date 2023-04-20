package com.hb.blog.view;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface UserView {
    public Long getId();
    @JsonProperty("first_name")
    public String getFirstName();
    @JsonProperty("last_name")
    public String getLastName();
    public Integer getAge();
    @JsonProperty("full_name")
    default String getFullName(){
        return getFirstName()+" "+getLastName();
    }
}
