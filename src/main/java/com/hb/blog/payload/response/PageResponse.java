package com.hb.blog.payload.response;

import java.util.List;

public record PageResponse<T>(Integer page, Integer size, Integer pages, Integer total, List<T> content) {
}
