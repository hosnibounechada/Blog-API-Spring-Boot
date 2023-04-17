package com.hb.blog.payload.response.user;

import java.util.List;

public record UsersResponse(Integer page, Integer size, Integer pages,Integer total, List<UserResponse> users) {
}
