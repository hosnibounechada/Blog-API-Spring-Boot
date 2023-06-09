package com.hb.blog.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.hb.blog.annotation.CustomAnnotation;
import com.hb.blog.exception.GoneException;
import com.hb.blog.mapper.UserMapper;
import com.hb.blog.model.Post;
import com.hb.blog.payload.response.user.UserResponse;
import com.hb.blog.payload.response.PageResponse;
import com.hb.blog.repository.PostRepository;
import com.hb.blog.view.PostView;
import com.hb.blog.view.UserViewImp;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hb.blog.exception.ConflictException;
import com.hb.blog.exception.NotFoundException;
import com.hb.blog.model.User;
import com.hb.blog.payload.request.user.CreateUserRequest;
import com.hb.blog.payload.request.user.UpdateUserRequest;
import com.hb.blog.repository.UserRepository;
import com.hb.blog.util.UpdateObject;

import static com.hb.blog.util.PaginationUtils.generatePageableResponse;


@Service
public class UserService implements IService<Long, CreateUserRequest, UpdateUserRequest, UserResponse> {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PostRepository postRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.userMapper = userMapper;
    }

    @Override
    public PageResponse<UserResponse> getAll() {
        List<User> users = userRepository.findAll();

        List<UserResponse> usersList = users.stream().map(userMapper::entityToResponse).collect(Collectors.toList());

        int count = usersList.size();

        return new PageResponse<>(0, count, count > 0 ? 1 : 0, count, usersList);
    }

    @Override
    public PageResponse<UserResponse> getByPages(int pageNumber, int pageSize, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(direction), sortBy));

        Page<User> pageUsers = userRepository.findAll(pageable);

        return generatePageableResponse(pageUsers, userMapper);
    }

    @Override
    @CustomAnnotation
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));

        return userMapper.entityToResponse(user);
    }

    @Override
    public UserResponse create(CreateUserRequest request) {
        /*validate(request);*/

        User user = userMapper.requestToEntity(request);

        return userMapper.entityToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse update(Long id, UpdateUserRequest request) {
        Optional<User> existedUser = userRepository.findById(id);

        if (existedUser.isEmpty()) throw new NotFoundException("User not found!");

        User updatedUser = existedUser.get();

        UpdateObject.updateUserFields(request, updatedUser);

        return userMapper.entityToResponse(userRepository.save(updatedUser));
    }

    /**
     * @deprecated (when, issue a request to check existing record before delete, refactoring advice...)
     */
    @Deprecated
    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) throw new NotFoundException("User already deleted!");

        userRepository.deleteById(id);

        if (userRepository.existsById(id)) throw new ConflictException("Couldn't delete user!");
    }

    @Override
    public void delete(Long id) {
        int count = userRepository.deleteUserById(id);

        if (count != 1) throw new GoneException("User already deleted!");
    }

    public PageResponse<UserViewImp> search(
            String firstName,
            String lastName,
            int pageNumber,
            int pageSize,
            String[] sortBy,
            String direction) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(direction), sortBy));


//        return userRepository.getUserByFirstNameOrLastNameOrderByFirstNameAscLastNameAsc(firstName, lastName);

        Page<UserViewImp> page = userRepository.getUserByFirstNameOrLastName(firstName, lastName, pageable);

        return generatePageableResponse(page);
    }

    public PageResponse<PostView> getUserPosts(Long id,
                                               int pageNumber,
                                               int pageSize,
                                               String[] sortBy,
                                               String direction) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(direction), sortBy));
        Page<PostView> page = postRepository.findByUserId(id, pageable);
        return generatePageableResponse(page);
    }

    public User getUsersPosts(Long userId) {
        return new User();
    }
}
