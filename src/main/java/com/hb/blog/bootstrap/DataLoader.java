package com.hb.blog.bootstrap;

import com.hb.blog.exception.NotFoundException;
import com.hb.blog.model.Post;
import com.hb.blog.model.User;
import com.hb.blog.repository.PostRepository;
import com.hb.blog.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public DataLoader(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadUsers();
        loadPosts();
    }

    public void loadUsers() {

        User hosni = new User("Hosni", "Bounechada", 28, "hosni@gmail.com", "azerty");
        User mohammed = new User("Mohammed", "Bounab", 26, "mohammed@gmail.com", "azerty");

        List<Post> posts = List.of(
                new Post(1L, "First Hosni Post", hosni),
                new Post(2L, "First Mohammed Post", mohammed)
        );
        postRepository.saveAll(posts);
    }

    public void loadPosts() {
        User hosni = userRepository.findById(1L).orElseThrow(() -> new NotFoundException("User Not Found!"));
        Post post = new Post(3L, "Second Hosni Post", hosni);
        postRepository.save(post);
    }
}
