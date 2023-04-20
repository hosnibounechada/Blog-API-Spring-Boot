package com.hb.blog.controller;

import com.hb.blog.annotation.CustomRequestParam;
import com.hb.blog.payload.request.user.CreateUserRequest;
import com.hb.blog.payload.request.user.UpdateUserRequest;
import com.hb.blog.payload.response.user.UserResponse;
import com.hb.blog.payload.response.user.PageResponse;
import com.hb.blog.service.UserService;
import com.hb.blog.util.StringLowerCaseEditor;
import com.hb.blog.view.UserView;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController implements IController<Long, CreateUserRequest, UpdateUserRequest, UserResponse> {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Convert all QueryParams to lower case
     * @return String
     */
    /* @InitBinder
    public void initBinder( WebDataBinder dataBinder )
    {
        StringLowerCaseEditor lowerCaseEditor = new StringLowerCaseEditor();
        dataBinder.registerCustomEditor( String.class, lowerCaseEditor );
    }*/

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
    public ResponseEntity<UserResponse> create(@RequestBody @Valid CreateUserRequest request) {
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
//        userService.delete(id);
        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserView>> search(@RequestParam(defaultValue = "") String firstName,
                                               @RequestParam(defaultValue = "") String lastName, String res) {
        System.out.println("========================================");
        System.out.println("|                                       |");
        System.out.println("|              " + res + "                    |");
        System.out.println("|                                       |");
        System.out.println("========================================");

        List<UserView> users = userService.search(firstName, lastName);
        return ResponseEntity.ok(users);
    }
}
