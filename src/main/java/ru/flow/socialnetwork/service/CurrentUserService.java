package ru.flow.socialnetwork.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.flow.socialnetwork.entity.User;
import ru.flow.socialnetwork.exception.UserNotFoundException;
import ru.flow.socialnetwork.repository.UserRepository;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Возвращает entity User текущего аутентифицированного пользователя.
     * Работает и для обычной сессии, и для Remember-Me.
     */
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            throw new UserNotFoundException("Пользователь не аутентифицирован");
        }
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        "Аутентифицированный пользователь не найден: " + username));
    }
}