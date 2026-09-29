package ru.flow.socialnetwork.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.flow.socialnetwork.dto.RegisterRequest;
import ru.flow.socialnetwork.entity.User;
import ru.flow.socialnetwork.exception.UserAlreadyExistsException;
import ru.flow.socialnetwork.exception.UserNotFoundException;
import ru.flow.socialnetwork.repository.UserRepository;

@Service
public class UserService {

    private static final int BCRYPT_ROUNDS = 12;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User register(RegisterRequest request, String ipAddress) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Пользователь с таким логином уже существует");
        }
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt(BCRYPT_ROUNDS));
        User user = new User(request.getUsername(), request.getEmail(), hashedPassword, 0);
        user.setIpAddress(ipAddress);
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + username));
    }

    public boolean checkPassword(String rawPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(rawPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        if (!checkPassword(password, user.getPassword())) {
            throw new UserNotFoundException("Неверный пароль");
        }
        return user;
    }
}