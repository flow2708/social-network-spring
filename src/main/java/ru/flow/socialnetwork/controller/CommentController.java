package ru.flow.socialnetwork.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.flow.socialnetwork.dto.CommentRequest;
import ru.flow.socialnetwork.entity.User;
import ru.flow.socialnetwork.service.CommentService;
import ru.flow.socialnetwork.service.CurrentUserService;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final CurrentUserService currentUserService;

    public CommentController(CommentService commentService,
                             CurrentUserService currentUserService) {
        this.commentService = commentService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/{postId}")
    public String list(@PathVariable Long postId, Model model) {
        model.addAttribute("postId", postId);
        model.addAttribute("comments", commentService.getPostComments(postId));
        model.addAttribute("commentRequest", new CommentRequest());
        return "comment/list";
    }

    @PostMapping("/{postId}")
    public String create(@PathVariable Long postId,
                         @Valid @ModelAttribute CommentRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("postId", postId);
            model.addAttribute("comments", commentService.getPostComments(postId));
            return "comment/list";
        }
        User user = currentUserService.getCurrentUser();
        commentService.createComment(postId, user.getUsername(), request.getContent());
        return "redirect:/comments/" + postId;
    }
}