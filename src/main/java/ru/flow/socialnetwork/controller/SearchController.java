package ru.flow.socialnetwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.flow.socialnetwork.entity.Post;
import ru.flow.socialnetwork.entity.User;
import ru.flow.socialnetwork.service.CurrentUserService;
import ru.flow.socialnetwork.service.FriendService;
import ru.flow.socialnetwork.service.LikeService;
import ru.flow.socialnetwork.service.PostService;
import ru.flow.socialnetwork.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController {

    private final UserService userService;
    private final FriendService friendService;
    private final PostService postService;
    private final LikeService likeService;
    private final CurrentUserService currentUserService;

    public SearchController(UserService userService,
                            FriendService friendService,
                            PostService postService,
                            LikeService likeService,
                            CurrentUserService currentUserService) {
        this.userService = userService;
        this.friendService = friendService;
        this.postService = postService;
        this.likeService = likeService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/search")
    public String search(@RequestParam String query, Model model) {
        User currentUser = currentUserService.getCurrentUser();
        User foundUser = userService.findByUsername(query);

        if (foundUser.getUsername().equals(currentUser.getUsername())) {
            return "redirect:/profile";
        }

        List<Post> posts = postService.getUserPosts(foundUser.getUsername());
        Map<Long, Boolean> likedMap = new HashMap<>();
        for (Post post : posts) {
            likedMap.put(post.getPostId(), likeService.isUserLiked(post.getPostId(), currentUser.getUsername()));
        }

        model.addAttribute("foundUser", foundUser);
        model.addAttribute("status",
                friendService.getFriendshipStatus(currentUser.getUsername(), foundUser.getUsername()));
        model.addAttribute("requestId",
                friendService.getRequestId(currentUser.getUsername(), foundUser.getUsername()));
        model.addAttribute("posts", posts);
        model.addAttribute("likedMap", likedMap);
        return "search";
    }
}