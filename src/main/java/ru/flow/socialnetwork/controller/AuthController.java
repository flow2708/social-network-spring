package ru.flow.socialnetwork.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.flow.socialnetwork.dto.LoginRequest;
import ru.flow.socialnetwork.dto.RegisterRequest;
import ru.flow.socialnetwork.exception.UserAlreadyExistsException;
import ru.flow.socialnetwork.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest request,
                           BindingResult bindingResult,
                           HttpServletRequest httpRequest) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        String ip = httpRequest.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = httpRequest.getRemoteAddr();
        }
        try {
            userService.register(request, ip);
        } catch (UserAlreadyExistsException e) {
            bindingResult.rejectValue("username", "user.exists",
                    "Пользователь с таким логином уже существует");
            return "auth/register";
        }
        return "redirect:/login?registered=true";
    }
}