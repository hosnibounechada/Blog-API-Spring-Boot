package com.hb.blog.controller;

import com.hb.blog.payload.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface IController<ID, CREATE, UPDATE, RESPONSE> {
    ResponseEntity<PageResponse<RESPONSE>> getAll();

    ResponseEntity<PageResponse<RESPONSE>> getByPages(int page, int size, String sortBy, String direction);

    ResponseEntity<RESPONSE> getById(ID id);

    ResponseEntity<RESPONSE> create(CREATE request);

    ResponseEntity<RESPONSE> update(ID id, UPDATE request);

    ResponseEntity<Void> delete(ID id);
}
