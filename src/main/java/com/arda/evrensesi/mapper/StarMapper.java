package com.arda.evrensesi.mapper;

import com.arda.evrensesi.dto.StarCoordinatesDTO;
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

    public static StarCoordinatesDTO toPointDTO(Star star) {
        if (star == null) {
            return null;
        }

        return new StarCoordinatesDTO(
                star.getX(),
                star.getY()
        );
    }
}
