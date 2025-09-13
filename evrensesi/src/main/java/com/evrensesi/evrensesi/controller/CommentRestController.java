package com.evrensesi.evrensesi.controller;

import com.evrensesi.evrensesi.dto.CommentDto;
import com.evrensesi.evrensesi.mapper.CommentMapper;
import com.evrensesi.evrensesi.model.Comment;
import com.evrensesi.evrensesi.request.CommentRequest;
import com.evrensesi.evrensesi.request.SearchRequest;
import com.evrensesi.evrensesi.service.CommentCrudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("comment")
@RequiredArgsConstructor
public class CommentRestController {

    private CommentCrudService commentCrudService;
    private CommentMapper commentMapper;

    @PostMapping("/createcomment")
    ResponseEntity<Comment> createComment(@Valid @RequestBody CommentRequest commentRequest){
         return ResponseEntity.status(HttpStatus.CREATED).body(
                 this.commentCrudService.addComment(this.commentMapper.map(commentRequest)));
    }

   @GetMapping("/getrelatedcomments")
    ResponseEntity<List<CommentDto>> getRelatedComments(@Valid @RequestBody SearchRequest searchRequest){
       List<Comment> comments = this.commentCrudService.getRelatedComment(searchRequest);
       return ResponseEntity.ok(this.commentMapper.mapToList(comments));
    }

    @GetMapping("/getcomment")
    ResponseEntity<CommentDto> getComment(){
        return ResponseEntity.status(HttpStatus.OK).body(this.commentMapper.map(this.commentCrudService.getComment()));
    }
}
