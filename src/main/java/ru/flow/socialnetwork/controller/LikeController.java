package ru.flow.socialnetwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.flow.socialnetwork.entity.User;
import ru.flow.socialnetwork.service.CurrentUserService;
import ru.flow.socialnetwork.service.LikeService;

@Controller
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;
    private final CurrentUserService currentUserService;

    public LikeController(LikeService likeService,
                          CurrentUserService currentUserService) {
        this.likeService = likeService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/{postId}/like")
    public String like(@PathVariable Long postId,
                       @RequestParam(required = false) String redirect) {
        User user = currentUserService.getCurrentUser();
        likeService.likePost(postId, user.getUsername());
        return "redirect:" + (redirect != null ? redirect : "/profile");
    }

    @PostMapping("/{postId}/unlike")
    public String unlike(@PathVariable Long postId,
                         @RequestParam(required = false) String redirect) {
        User user = currentUserService.getCurrentUser();
        likeService.unlikePost(postId, user.getUsername());
        return "redirect:" + (redirect != null ? redirect : "/profile");
    }
}