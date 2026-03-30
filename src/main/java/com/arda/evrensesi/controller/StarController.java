package com.arda.evrensesi.controller;

import com.arda.evrensesi.dto.StarCoordinatesDTO;
import com.arda.evrensesi.dto.StarMessageDTO;
import com.arda.evrensesi.request.StarRequest;
import com.arda.evrensesi.service.StarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/star")
@Slf4j
@Tag(name = "Star", description = "Star operations")
public class StarController {

    private final StarService starService;

    @Operation(summary = "Create a star for the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Star created successfully"),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already has a star (star.already.exists) or coordinates are occupied (star.creation.conflict)",
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User not authenticated",
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    @PostMapping("/create")
    public ResponseEntity<Void> createStar(@Valid @RequestBody StarRequest starRequest) {
        starService.createStar(starRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get all star coordinates (paginated)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Star coordinates fetched successfully",
                    content = @Content(schema = @Schema(implementation = Page.class))
            )
    })
    @GetMapping("/points")
    public ResponseEntity<Page<StarCoordinatesDTO>> getAllStarCoordinates(@RequestParam int page,
                                                                          @RequestParam int size) {
        return ResponseEntity.ok(this.starService.getAllStarCoordinates(page, size));
    }

    @Operation(summary = "Get the authenticated user's star")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User star fetched successfully",
                    content = @Content(schema = @Schema(implementation = StarCoordinatesDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User has no star — star.user.not.found",
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User not authenticated",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    @GetMapping("/mystar")
    public ResponseEntity<StarCoordinatesDTO> getMyStar() {
        return ResponseEntity.ok(this.starService.getUserStar());
    }

    @Operation(summary = "Search stars by message keyword (uses Elasticsearch)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully",
                    content = @Content(schema = @Schema(implementation = StarCoordinatesDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No stars found for keyword — star.search.not.found",
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Keyword is null or blank — search.keyword.empty",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    @GetMapping("/search")
    public ResponseEntity<List<StarCoordinatesDTO>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(this.starService.search(keyword));
    }

    @Operation(summary = "Get star message by coordinates")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Star message fetched successfully",
                    content = @Content(schema = @Schema(implementation = StarMessageDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No star found at given coordinates — star.message.not.found",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    @GetMapping("/getMessage")
    public ResponseEntity<StarMessageDTO> getMessage(@RequestParam int x, @RequestParam int y) {
        return ResponseEntity.ok(this.starService.getStarMessage(x, y));
    }
}