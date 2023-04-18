package com.hb.blog.util;

import com.hb.blog.mapper.GenericMapper;
import com.hb.blog.payload.response.user.PageResponse;
import org.springframework.data.domain.Page;

public class Pagination {
    public static  <ENTITY, REQUEST, RESPONSE> PageResponse<RESPONSE> generateResponse(Page<ENTITY> page, GenericMapper<ENTITY, REQUEST, RESPONSE> mapper){

        return new PageResponse<>(page.getNumber(),page.getNumberOfElements(), page.getTotalPages(), (int) page.getTotalElements(), mapper.entitiesToResponses(page.getContent()));
    }
}
