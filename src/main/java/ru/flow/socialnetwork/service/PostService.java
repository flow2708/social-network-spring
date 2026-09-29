package ru.flow.socialnetwork.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.flow.socialnetwork.entity.Post;
import ru.flow.socialnetwork.entity.User;
import ru.flow.socialnetwork.exception.PostNotFoundException;
import ru.flow.socialnetwork.repository.PostRepository;
import ru.flow.socialnetwork.repository.UserRepository;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Post createPost(String username, String content) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Post post = new Post(user, content);
        return postRepository.save(post);
    }

    public List<Post> getUserPosts(String username) {
        return postRepository.findByUserUsernameOrderByCreatedAtDesc(username);
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Пост не найден: " + postId));
    }
}