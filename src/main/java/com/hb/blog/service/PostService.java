package com.hb.blog.service;

import com.hb.blog.exception.ConflictException;
import com.hb.blog.exception.GoneException;
import com.hb.blog.exception.NotFoundException;
import com.hb.blog.mapper.PostMapper;
import com.hb.blog.model.Post;
import com.hb.blog.model.User;
import com.hb.blog.payload.request.post.CreatePostRequest;
import com.hb.blog.payload.request.post.UpdatePostRequest;
import com.hb.blog.payload.response.post.PostResponse;
import com.hb.blog.payload.response.PageResponse;
import com.hb.blog.repository.PostRepository;
import com.hb.blog.repository.UserRepository;
import com.hb.blog.util.PaginationUtils;
import com.hb.blog.util.UpdateObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService implements IService<Long, CreatePostRequest, UpdatePostRequest, PostResponse> {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public PostService(UserRepository userRepository, PostRepository postRepository, PostMapper postMapper) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    public PageResponse<PostResponse> getAll() {
        List<Post> users = postRepository.findAll();

        List<PostResponse> postsList = users.stream().map(postMapper::entityToResponse).collect(Collectors.toList());

        int count = postsList.size();

        return new PageResponse<>(0, count, count > 0 ? 1 : 0, count, postsList);
    }

    @Override
    public PageResponse<PostResponse> getByPages(int pageNumber, int pageSize, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(direction), sortBy));

        Page<Post> pageUsers = postRepository.findAll(pageable);

        return PaginationUtils.generatePageableResponse(pageUsers, postMapper);
    }

    @Override
    public PostResponse getById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));

        return postMapper.entityToResponse(post);
    }

    @Override
    public PostResponse create(CreatePostRequest request) {
        User user = userRepository.findById(request.userId()).orElseThrow(() -> new NotFoundException("User not found"));

        Post post = new Post(request.content(), user);

        return postMapper.entityToResponse(postRepository.save(post));
    }

    @Override
    public PostResponse update(Long id, UpdatePostRequest request) {
        Optional<Post> existedPost = postRepository.findById(id);

        if (existedPost.isEmpty()) throw new NotFoundException("Post not found!");

        Post updatedPost = existedPost.get();

        UpdateObject.updatePostFields(request, updatedPost);

        return postMapper.entityToResponse(postRepository.save(updatedPost));
    }

    @Deprecated
    @Override
    public void deleteById(Long id) {
        if (!postRepository.existsById(id)) throw new NotFoundException("Post already deleted!");

        postRepository.deleteById(id);

        if (postRepository.existsById(id)) throw new ConflictException("Couldn't delete post!");
    }

    @Override
    public void delete(Long id) {
        int count = postRepository.deletePostById(id);

        if (count != 1) throw new GoneException("Post already deleted!");
    }
}
