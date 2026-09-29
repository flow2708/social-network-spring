package ru.flow.socialnetwork.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.flow.socialnetwork.entity.Comment;
import ru.flow.socialnetwork.entity.Post;
import ru.flow.socialnetwork.entity.User;
import ru.flow.socialnetwork.repository.CommentRepository;
import ru.flow.socialnetwork.repository.PostRepository;
import ru.flow.socialnetwork.repository.UserRepository;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Comment createComment(Long postId, String username, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Пост не найден"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Comment comment = new Comment(post, user, content);
        return commentRepository.save(comment);
    }

    public List<Comment> getPostComments(Long postId) {
        return commentRepository.findByPostPostIdOrderByCreatedAtAsc(postId);
    }
}