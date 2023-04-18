package com.hb.blog.controller;

import com.hb.blog.payload.request.user.RegisterRequest;
import com.hb.blog.payload.request.user.UpdateUserRequest;
import com.hb.blog.payload.response.user.UserResponse;
import com.hb.blog.payload.response.user.PageResponse;
import com.hb.blog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController implements IController<Long, RegisterRequest, UpdateUserRequest, UserResponse> {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<PageResponse<UserResponse>> getAll() {
        PageResponse<UserResponse> pageResponse = userService.getAll();

        return ResponseEntity.ok(pageResponse);
    }
    @Override
    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> getByPages(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                    @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(userService.getByPages(page, size, sortBy, direction));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
