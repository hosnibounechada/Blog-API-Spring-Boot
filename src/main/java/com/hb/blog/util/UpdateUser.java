package com.hb.blog.util;

import com.hb.blog.dto.UserDTO;
import com.hb.blog.model.User;

public class UpdateUser {
    public static void updateUserFields(UserDTO userDTO, User user){
        if(userDTO.getFirstName() != null) user.setFirstName(userDTO.getFirstName());
        if(userDTO.getLastName() != null) user.setLastName(userDTO.getLastName());
        if(userDTO.getAge() != null) user.setAge(userDTO.getAge());
    }
}
