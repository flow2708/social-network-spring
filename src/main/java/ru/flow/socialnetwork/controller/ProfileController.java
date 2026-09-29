package ru.flow.socialnetwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.flow.socialnetwork.entity.Post;
import ru.flow.socialnetwork.entity.User;
import ru.flow.socialnetwork.service.CurrentUserService;
import ru.flow.socialnetwork.service.LikeService;
import ru.flow.socialnetwork.service.PostService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProfileController {

    private final PostService postService;
    private final LikeService likeService;
    private final CurrentUserService currentUserService;

    public ProfileController(PostService postService,
                             LikeService likeService,
                             CurrentUserService currentUserService) {
        this.postService = postService;
        this.likeService = likeService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        User user = currentUserService.getCurrentUser();
        List<Post> posts = postService.getUserPosts(user.getUsername());

        // Карта postId -> isLiked
        Map<Long, Boolean> likedMap = new HashMap<>();
        for (Post post : posts) {
            likedMap.put(post.getPostId(), likeService.isUserLiked(post.getPostId(), user.getUsername()));
        }

        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("likedMap", likedMap);
        return "profile";
    }
}