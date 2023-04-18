package com.hb.blog.service;

import com.hb.blog.payload.response.user.PageResponse;

public interface IService<ID, CREATE, UPDATE, RESPONSE> {
    PageResponse<RESPONSE> getAll();
    PageResponse<RESPONSE> getByPages(int pageNumber, int pageSize, String sortBy, String direction);
    RESPONSE getById(ID id);
    RESPONSE create(CREATE request);
    RESPONSE update(ID id, UPDATE request);
    boolean deleteById(ID id);
    void delete(ID id);
}
