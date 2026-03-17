package com.arda.evrensesi.service.impl;

import com.arda.evrensesi.model.entity.Star;
import com.arda.evrensesi.repository.StarRepository;
import com.arda.evrensesi.service.StarIndexStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class StarIndexStatusServiceImpl implements StarIndexStatusService {

    private final StarRepository starRepository;

    public StarIndexStatusServiceImpl(StarRepository starRepository) {
        this.starRepository = starRepository;
    }
   @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markIndexed(UUID starId) {
        Star star = starRepository.findById(starId)
                .orElseThrow(() -> new IllegalStateException("Star not found. id=" + starId));

        star.setEsIndexed(true);
        starRepository.save(star);
    }
}
