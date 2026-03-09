package com.arda.evrensesi.controller;

import com.arda.evrensesi.dto.StarCoordinatesDTO;
import com.arda.evrensesi.request.StarRequest;
import com.arda.evrensesi.service.StarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/star")
public class StarController {

    private final StarService starService;

    @PostMapping("/create")
    public ResponseEntity<Void> createStar(@Valid @RequestBody StarRequest starRequest) {
        starService.createStar(starRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/points")
    public ResponseEntity<Page<StarCoordinatesDTO>> getAllStarCoordinates(
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok(this.starService.getAllStarCoordinates(page, size));
    }

}
