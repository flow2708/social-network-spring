package ru.flow.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.flow.socialnetwork.entity.Like;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostPostIdAndUserUsername(Long postId, String username);
    boolean existsByPostPostIdAndUserUsername(Long postId, String username);
    void deleteByPostPostIdAndUserUsername(Long postId, String username);
}