package ru.flow.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.flow.socialnetwork.entity.Comment;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostPostIdOrderByCreatedAtAsc(Long postId);
}