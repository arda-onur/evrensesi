package com.arda.evrensesi.service;

import com.arda.evrensesi.dto.StarCoordinatesDTO;
import com.arda.evrensesi.model.document.StarDocument;
import com.arda.evrensesi.request.StarRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StarService {
    StarCoordinatesDTO createStar(StarRequest starRequest);
    Page<StarCoordinatesDTO> getAllStarCoordinates(int page, int size);
    StarCoordinatesDTO getUserStar();
    List<StarCoordinatesDTO> search(String keyword);
}
