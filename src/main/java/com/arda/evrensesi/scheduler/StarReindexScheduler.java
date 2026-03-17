package com.arda.evrensesi.scheduler;

import com.arda.evrensesi.mapper.search.StarDocumentMapper;
import com.arda.evrensesi.model.document.StarDocument;
import com.arda.evrensesi.model.entity.Star;
import com.arda.evrensesi.repository.StarESRepository;
import com.arda.evrensesi.repository.StarRepository;
import com.arda.evrensesi.service.StarIndexStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class StarReindexScheduler {
    private final StarRepository starRepository;
    private final StarIndexStatusService starIndexStatusService;
    private final StarESRepository starESRepository;


    public StarReindexScheduler(StarRepository starRepository,
                                StarIndexStatusService starIndexStatusService,
                                StarESRepository starESRepository) {
        this.starRepository = starRepository;
        this.starIndexStatusService = starIndexStatusService;
        this.starESRepository = starESRepository;
    }

    @Scheduled(initialDelay = 30000, fixedDelay = 3600000)
    public void retryUnwrittenMessages(){
        List<Star> unwrittenMessages = findUnindexedStars();

        if(unwrittenMessages.isEmpty()) return;

        unwrittenMessages.stream().forEach(star -> {
            try {
                StarDocument starDocument = StarDocumentMapper.toDocument(star);
                this.starESRepository.save(starDocument);
                this.starIndexStatusService.markIndexed(star.getId());
            }catch (Exception e){
                log.error("Failed to index star. starId={}", star.getId(), e);
            }
        });
    }


  private List<Star> findUnindexedStars(){
         return this.starRepository.findAllByEsIndexedFalse();
    }
}
