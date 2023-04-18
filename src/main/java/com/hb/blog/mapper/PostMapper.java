package com.hb.blog.mapper;

import com.hb.blog.model.Post;
import com.hb.blog.payload.request.post.CreatePostRequest;
import com.hb.blog.payload.response.post.PostResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post fromCreateRequestToPost(CreatePostRequest createRequest);
    PostResponse fromPostToPostResponse(Post post);
}
