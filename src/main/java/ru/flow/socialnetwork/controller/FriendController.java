package ru.flow.socialnetwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.flow.socialnetwork.entity.User;
import ru.flow.socialnetwork.service.CurrentUserService;
import ru.flow.socialnetwork.service.FriendService;

@Controller
@RequestMapping("/friendship")
public class FriendController {

    private final FriendService friendService;
    private final CurrentUserService currentUserService;

    public FriendController(FriendService friendService,
                            CurrentUserService currentUserService) {
        this.friendService = friendService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/send")
    public String send(@RequestParam String target) {
        User user = currentUserService.getCurrentUser();
        friendService.sendFriendRequest(user.getUsername(), target);
        return "redirect:/search?query=" + target;
    }

    @PostMapping("/accept")
    public String accept(@RequestParam Long requestId) {
        User user = currentUserService.getCurrentUser();
        friendService.acceptFriendRequest(requestId, user.getUsername());
        return "redirect:/notifications";
    }

    @PostMapping("/reject")
    public String reject(@RequestParam Long requestId) {
        User user = currentUserService.getCurrentUser();
        friendService.rejectFriendRequest(requestId, user.getUsername());
        return "redirect:/notifications";
    }

    @PostMapping("/cancel")
    public String cancel(@RequestParam Long requestId) {
        User user = currentUserService.getCurrentUser();
        friendService.cancelFriendRequest(requestId, user.getUsername());
        return "redirect:/notifications";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam String target) {
        User user = currentUserService.getCurrentUser();
        friendService.removeFriend(user.getUsername(), target);
        return "redirect:/friendsList";
    }
}