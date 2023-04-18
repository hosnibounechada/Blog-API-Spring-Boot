package com.hb.blog.util;

import com.hb.blog.mapper.GenericMapper;
import com.hb.blog.payload.response.user.PageResponse;
import org.springframework.data.domain.Page;

public class Pagination {
    public static  <T, S> PageResponse<S> generateResponse(Page<T> page, GenericMapper<T, S> mapper){

        return new PageResponse<>(page.getNumber(),page.getNumberOfElements(), page.getTotalPages(), (int) page.getTotalElements(), mapper.to(page.getContent()));
    }
}
