package com.hb.blog.util;

import com.hb.blog.mapper.GenericMapper;
import com.hb.blog.payload.response.PageResponse;
import org.springframework.data.domain.Page;

public class PaginationUtils {
    public static  <ENTITY, REQUEST, RESPONSE> PageResponse<RESPONSE> generatePageableResponse(Page<ENTITY> page, GenericMapper<ENTITY, REQUEST, RESPONSE> mapper){

        return new PageResponse<>(page.getNumber(),page.getNumberOfElements(), page.getTotalPages(), (int) page.getTotalElements(), mapper.entitiesToResponses(page.getContent()));
    }
    public static  <VIEW> PageResponse<VIEW> generatePageableResponse(Page<VIEW> page){

        return new PageResponse<>(page.getNumber(),page.getNumberOfElements(), page.getTotalPages(), (int) page.getTotalElements(), page.getContent());
    }
}
