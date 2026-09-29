package ru.flow.socialnetwork.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.flow.socialnetwork.entity.Like;
import ru.flow.socialnetwork.entity.Post;
import ru.flow.socialnetwork.entity.User;
import ru.flow.socialnetwork.repository.LikeRepository;
import ru.flow.socialnetwork.repository.PostRepository;
import ru.flow.socialnetwork.repository.UserRepository;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository,
                       PostRepository postRepository,
                       UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean likePost(Long postId, String username) {
        if (likeRepository.existsByPostPostIdAndUserUsername(postId, username)) {
            return false;
        }
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Пост не найден"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Like like = new Like(post, user);
        likeRepository.save(like);
        postRepository.incrementLikeCount(postId);
        return true;
    }

    @Transactional
    public boolean unlikePost(Long postId, String username) {
        if (!likeRepository.existsByPostPostIdAndUserUsername(postId, username)) {
            return false;
        }
        likeRepository.deleteByPostPostIdAndUserUsername(postId, username);
        postRepository.decrementLikeCount(postId);
        return true;
    }

    public boolean isUserLiked(Long postId, String username) {
        return likeRepository.existsByPostPostIdAndUserUsername(postId, username);
    }
}