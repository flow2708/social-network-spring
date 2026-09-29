package ru.flow.socialnetwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.flow.socialnetwork.entity.User;
import ru.flow.socialnetwork.service.CurrentUserService;
import ru.flow.socialnetwork.service.FriendService;

@Controller
public class NotificationsController {

    private final FriendService friendService;
    private final CurrentUserService currentUserService;

    public NotificationsController(FriendService friendService,
                                   CurrentUserService currentUserService) {
        this.friendService = friendService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/notifications")
    public String notifications(Model model) {
        User user = currentUserService.getCurrentUser();
        model.addAttribute("senders", friendService.getFriendRequestSenders(user.getUsername()));
        return "notifications";
    }

    @GetMapping("/friendsList")
    public String friendsList(Model model) {
        User user = currentUserService.getCurrentUser();
        model.addAttribute("friends", friendService.getFriendsList(user.getUsername()));
        return "friendsList";
    }
}