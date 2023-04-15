package com.hb.blog.service;

import com.hb.blog.dto.UserDTO;
import com.hb.blog.exception.ConflictException;
import com.hb.blog.exception.NotFoundException;
import com.hb.blog.exception.ResourceAlreadyExistsException;
import com.hb.blog.mapper.UserMapper;
import com.hb.blog.model.User;
import com.hb.blog.payload.request.RegisterRequest;
import com.hb.blog.repository.UserRepository;
import com.hb.blog.util.UpdateUser;
import com.hb.blog.validator.ObjectValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> userMapper.fromUserToUserDTO(user)).collect(Collectors.toList());
    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));

        return userMapper.fromUserToUserDTO(user);
    }

    public UserDTO create(RegisterRequest registerRequest) {
        validator.validate(registerRequest);

        /*if (userRepository.existsByEmail(registerRequest.getEmail()))
            throw new ResourceAlreadyExistsException("Error: Email is already in use!");*/

        User user = userMapper.fromRegisterRequestToUser(registerRequest);

        return userMapper.fromUserToUserDTO(userRepository.save(user));
    }

    public UserDTO update(Long id, UserDTO userDTO) {
        Optional<User> existedUser = userRepository.findById(id);

        if (!existedUser.isPresent()) throw new NotFoundException("User not found!");

        User updatedUser = existedUser.get();

        UpdateUser.updateUserFields(userDTO, updatedUser);

        return userMapper.fromUserToUserDTO(userRepository.save(updatedUser));
    }

    @Deprecated
    public boolean deleteUserById(Long id) {
        if (!userRepository.existsById(id)) throw new NotFoundException("User already deleted!");

        userRepository.deleteById(id);

        if (userRepository.existsById(id)) throw new ConflictException("Couldn't delete user!");
        return true;
    }

    public boolean delete(Long id) {
        int count = userRepository.deleteUserById(id);

        if (count != 1) throw new NotFoundException("User already deleted!");

        return true;
    }
}
