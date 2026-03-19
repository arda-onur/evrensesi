package com.arda.evrensesi.repository;

import com.arda.evrensesi.model.document.StarDocument;
import com.arda.evrensesi.model.entity.Star;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StarESRepository extends ElasticsearchRepository<StarDocument, String> {
    List<StarDocument> findByMessageContaining(String keyword);
}
