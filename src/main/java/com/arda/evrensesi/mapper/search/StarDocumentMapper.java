package com.arda.evrensesi.mapper.search;

import com.arda.evrensesi.event.StarCreatedEvent;
import com.arda.evrensesi.model.document.StarDocument;
import com.arda.evrensesi.model.entity.Star;

public class StarDocumentMapper {

    private StarDocumentMapper() {}

    public static StarDocument toDocument(StarCreatedEvent star){
        return new StarDocument(star.id().toString(),
                                star.message(),
                                star.x(),
                                star.y());
    }
    public static StarDocument toDocument(Star star) {
        return new StarDocument(
                star.getId().toString(),
                star.getMessage(),
                star.getX(),
                star.getY()
        );
    }
}
