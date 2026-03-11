package com.arda.evrensesi.service;

import com.arda.evrensesi.dto.StarCoordinatesDTO;
import com.arda.evrensesi.request.StarRequest;
import org.springframework.data.domain.Page;

public interface StarService {
    StarCoordinatesDTO createStar(StarRequest starRequest);
    Page<StarCoordinatesDTO> getAllStarCoordinates(int page, int size);
}
