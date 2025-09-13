package com.evrensesi.evrensesi.service.implementation;

import com.evrensesi.evrensesi.dto.CommentDto;
import com.evrensesi.evrensesi.exception.CommentAlreadyExistsException;
import com.evrensesi.evrensesi.exception.NoSuchCommentException;
import com.evrensesi.evrensesi.model.Comment;
import com.evrensesi.evrensesi.model.User;
import com.evrensesi.evrensesi.repository.CommentRepository;
import com.evrensesi.evrensesi.request.SearchRequest;
import com.evrensesi.evrensesi.service.CommentCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CommentCrudServiceImpl implements CommentCrudService {

    private final CommentRepository commentRepository;

    @Override
    public Comment addComment(Comment comment) {
    User logedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    boolean isCommentCreated = this.commentRepository.findByUser_Username(logedInUser.getUsername()).isPresent();
    if (isCommentCreated) {
        throw new CommentAlreadyExistsException("Comment already exists for " + logedInUser.getUsername());
    }
      comment.setUser(logedInUser);
    this.commentRepository.save(comment);
    return comment;
    }
    @Override
    public Comment getComment() {
        User username = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.commentRepository.findCommentByUser_Username(username.getUsername())
                .orElseThrow(() -> new NoSuchCommentException("No comment found for username: " + username.getUsername()));
    }

    @Override
    public List<Comment> getRelatedComment(SearchRequest searchRequest) {
        List<Comment> comments = this.commentRepository.findByCommentContaining(searchRequest.search());
        if (comments.isEmpty()) {
            throw new NoSuchCommentException("No such comment for search: " + searchRequest.search());
        }
        return comments;
    }

}
