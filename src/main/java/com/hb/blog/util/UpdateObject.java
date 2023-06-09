package com.hb.blog.util;

import com.hb.blog.model.Post;
import com.hb.blog.model.User;
import com.hb.blog.payload.request.post.UpdatePostRequest;
import com.hb.blog.payload.request.user.UpdateUserRequest;

public class UpdateObject {
    public static void updateUserFields(UpdateUserRequest updateUserRequest, User user){
        /*if(updateUserRequest.firstName() != null) user.setFirstName(updateUserRequest.firstName());
        if(updateUserRequest.lastName() != null) user.setLastName(updateUserRequest.lastName());
        if(updateUserRequest.age() != null) user.setAge(updateUserRequest.age());*/
        if(updateUserRequest.getFirstName() != null) user.setFirstName(updateUserRequest.getFirstName());
        if(updateUserRequest.getLastName() != null) user.setLastName(updateUserRequest.getLastName());
        if(updateUserRequest.getAge() != null) user.setAge(updateUserRequest.getAge());
    }

    public static void updatePostFields(UpdatePostRequest updatePostRequest, Post post){
        if(updatePostRequest.content() != null) post.setContent(updatePostRequest.content());
    }
}
