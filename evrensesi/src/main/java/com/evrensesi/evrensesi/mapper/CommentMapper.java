package com.evrensesi.evrensesi.mapper;

import com.evrensesi.evrensesi.dto.CommentDto;
import com.evrensesi.evrensesi.model.Comment;
import com.evrensesi.evrensesi.request.CommentRequest;
import com.evrensesi.evrensesi.request.SearchRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(config = MapperConfiguration.class)
@Component
public interface CommentMapper {

    Comment map(CommentRequest commentRequest);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Comment map (SearchRequest searchRequest);

    CommentDto map (Comment comment);

    List<CommentDto> mapToList (List<Comment> comments);

}
