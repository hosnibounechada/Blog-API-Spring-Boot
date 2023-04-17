package com.hb.blog.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hb.blog.exception.GoneException;
import com.hb.blog.payload.response.user.UserResponse;
import com.hb.blog.payload.response.user.UsersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hb.blog.exception.ConflictException;
import com.hb.blog.exception.NotFoundException;
import com.hb.blog.mapper.UserMapper;
import com.hb.blog.model.User;
import com.hb.blog.payload.request.user.RegisterRequest;
import com.hb.blog.payload.request.user.UpdateUserRequest;
import com.hb.blog.repository.UserRepository;
import com.hb.blog.util.UpdateUser;
import com.hb.blog.validator.ObjectValidator;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ObjectValidator validator;

    public UserService(UserRepository userRepository, UserMapper userMapper, ObjectValidator validator) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.validator = validator;
    }

    public UsersResponse findAll() {
        List<User> users = userRepository.findAll();

        List<UserResponse> usersList = users.stream().map(userMapper::fromUserToUserResponse).collect(Collectors.toList());

        int count = usersList.size();

        return new UsersResponse(0,count, count > 0 ? 1 : 0, count, usersList);
    }

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));

        return userMapper.fromUserToUserResponse(user);
    }

    public UserResponse create(RegisterRequest registerRequest) {
        validator.validate(registerRequest);

        /*if (userRepository.existsByEmail(registerRequest.getEmail()))
            throw new ResourceAlreadyExistsException("Error: Email is already in use!");*/

        User user = userMapper.fromRegisterRequestToUser(registerRequest);

        return userMapper.fromUserToUserResponse(userRepository.save(user));
    }

    public UserResponse update(Long id, UpdateUserRequest updateUserRequest) {
        Optional<User> existedUser = userRepository.findById(id);

        if (existedUser.isEmpty()) throw new NotFoundException("User not found!");

        User updatedUser = existedUser.get();

        UpdateUser.updateUserFields(updateUserRequest, updatedUser);
//        CloneObjectFields.cloneNonNullProperties(userDTO, updatedUser);

        return userMapper.fromUserToUserResponse(userRepository.save(updatedUser));
    }

    /**
     * @deprecated (when, issue a request to check existing record before delete, refactoring advice...)
     */
    @Deprecated
    public boolean deleteUserById(Long id) {
        if (!userRepository.existsById(id)) throw new NotFoundException("User already deleted!");

        userRepository.deleteById(id);

        if (userRepository.existsById(id)) throw new ConflictException("Couldn't delete user!");
        return true;
    }

    public void delete(Long id) {
        int count = userRepository.deleteUserById(id);

        if (count != 1) throw new GoneException("User already deleted!");
    }

    public UsersResponse getUsers(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<User> pageUsers = userRepository.findAll(pageable);

        List<UserResponse> users = pageUsers.stream().map(userMapper::fromUserToUserResponse).collect(Collectors.toList());

        return new UsersResponse(pageUsers.getNumber(),users.size(), pageUsers.getTotalPages(), (int) pageUsers.getTotalElements(), users);
    }
}
