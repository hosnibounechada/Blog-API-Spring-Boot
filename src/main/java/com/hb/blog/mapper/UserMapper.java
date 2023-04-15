package com.hb.blog.mapper;

import com.hb.blog.dto.UserDTO;
import com.hb.blog.model.User;
import com.hb.blog.payload.request.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    UserDTO fromUserToUserDTO(User user);
    User fromUserDTOToUser(UserDTO userDTO);
    User fromRegisterRequestToUser(RegisterRequest registerRequest);
}
