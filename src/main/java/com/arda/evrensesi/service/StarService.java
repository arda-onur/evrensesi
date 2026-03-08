package com.arda.evrensesi.service;

import com.arda.evrensesi.dto.StarPointDTO;
import com.arda.evrensesi.request.StarRequest;


public interface StarService {
    StarPointDTO createStar(StarRequest starRequest);
}
