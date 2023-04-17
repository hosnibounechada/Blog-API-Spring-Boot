package com.hb.blog.util;

import com.hb.blog.model.User;
import com.hb.blog.payload.request.user.UpdateUserRequest;

public class UpdateUser {
    public static void updateUserFields(UpdateUserRequest updateUserRequest, User user){
        if(updateUserRequest.firstName() != null) user.setFirstName(updateUserRequest.firstName());
        if(updateUserRequest.lastName() != null) user.setLastName(updateUserRequest.lastName());
        if(updateUserRequest.age() != null) user.setAge(updateUserRequest.age());
    }
}
