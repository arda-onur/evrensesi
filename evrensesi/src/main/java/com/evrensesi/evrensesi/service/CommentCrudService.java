package com.evrensesi.evrensesi.service;

import com.evrensesi.evrensesi.dto.CommentDto;
import com.evrensesi.evrensesi.model.Comment;
import com.evrensesi.evrensesi.request.SearchRequest;

import java.util.List;

public interface CommentCrudService {

    Comment addComment(Comment comment);

    Comment getComment();

    List<Comment> getRelatedComment(SearchRequest searchRequest);
}
