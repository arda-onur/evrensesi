package com.evrensesi.evrensesi.repository;

import com.evrensesi.evrensesi.model.Comment;
import com.evrensesi.evrensesi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findByUser_Username(String userUsername);

    List<Comment> findByCommentContaining(String comment);

    Optional<Comment> findCommentByUser(User user);

    Optional<Comment> findCommentByUser_Username(String userUsername);
}
