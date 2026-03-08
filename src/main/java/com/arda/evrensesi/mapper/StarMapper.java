package com.arda.evrensesi.mapper;

import com.arda.evrensesi.dto.StarPointDTO;
import com.arda.evrensesi.entity.Star;
import com.arda.evrensesi.request.StarRequest;

public class StarMapper {

    private StarMapper() {
    }

    public static Star toEntity(StarRequest request) {
        Star star = new Star();

        star.setMessage(request.message());
        star.setX(request.x());
        star.setY(request.y());

        return star;
    }

    public static StarPointDTO toPointDTO(Star star) {
        if (star == null) {
            return null;
        }

        return new StarPointDTO(
                star.getX(),
                star.getY()
        );
    }
}
