package com.hb.blog.mapper;

import com.hb.blog.model.Post;
import com.hb.blog.payload.response.post.PostResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapperGeneric extends GenericMapper<Post, PostResponse>{

}
