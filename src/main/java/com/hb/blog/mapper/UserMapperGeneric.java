package com.hb.blog.mapper;

import com.hb.blog.model.User;
import com.hb.blog.payload.response.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapperGeneric extends GenericMapper<User, UserResponse>{
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    UserResponse to(User user);
}
