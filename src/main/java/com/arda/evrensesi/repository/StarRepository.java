package com.arda.evrensesi.repository;

import com.arda.evrensesi.dto.StarPointDTO;
import com.arda.evrensesi.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StarRepository extends JpaRepository<Star, UUID> {

    @Query("""
                SELECT new com.arda.evrensesi.dto.StarPointDTO(s.x, s.y)
            FROM Star s
            """)
    List<StarPointDTO> findAllPoints();


    boolean existsByUserEmail(String email);
}
