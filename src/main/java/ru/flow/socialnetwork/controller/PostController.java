package ru.flow.socialnetwork.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.flow.socialnetwork.dto.PostRequest;
import ru.flow.socialnetwork.entity.User;
import ru.flow.socialnetwork.service.CurrentUserService;
import ru.flow.socialnetwork.service.PostService;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CurrentUserService currentUserService;

    public PostController(PostService postService,
                          CurrentUserService currentUserService) {
        this.postService = postService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("postRequest", new PostRequest());
        return "post/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute PostRequest request,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "post/create";
        }
        User user = currentUserService.getCurrentUser();
        postService.createPost(user.getUsername(), request.getContent());
        return "redirect:/profile";
    }
}