package ru.flow.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.flow.socialnetwork.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}