package com.arda.evrensesi.service.impl;

import com.arda.evrensesi.dto.StarCoordinatesDTO;
import com.arda.evrensesi.dto.StarMessageDTO;
import com.arda.evrensesi.event.StarCreatedEvent;
import com.arda.evrensesi.exception.customException.StarAlreadyExistsException;
import com.arda.evrensesi.exception.customException.StarCreationException;
import com.arda.evrensesi.exception.customException.StarNotFoundException;
import com.arda.evrensesi.mapper.api.StarMapper;
import com.arda.evrensesi.mapper.search.StarDocumentMapper;
import com.arda.evrensesi.model.entity.Star;
import com.arda.evrensesi.model.entity.User;
import com.arda.evrensesi.repository.StarESRepository;
import com.arda.evrensesi.repository.StarRepository;
import com.arda.evrensesi.repository.UserRepository;
import com.arda.evrensesi.request.StarRequest;
import com.arda.evrensesi.service.StarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class StarServiceImpl implements StarService {

    private final StarRepository starRepository;
    private final UserRepository userRepository;
    private final StarESRepository starESRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public StarServiceImpl(StarRepository starRepository,
                           UserRepository userRepository,
                           StarESRepository starESRepository,
                           ApplicationEventPublisher applicationEventPublisher) {
        this.starRepository = starRepository;
        this.userRepository = userRepository;
        this.starESRepository = starESRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public StarCoordinatesDTO createStar(StarRequest starRequest) {
        String userEmail = getUserEmail();

        log.info("Star creation requested by user={}", userEmail);

        if (this.starRepository.existsByUserEmail(userEmail)) {
            log.warn("Star creation rejected because user already has a star. user={}", userEmail);
            throw new StarAlreadyExistsException("star.already.exists", userEmail);
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    log.warn("Star creation failed because user was not found. user={}", userEmail);
                    return new UsernameNotFoundException("star.creation.must.be.login");
                });

        try {
            Star star = StarMapper.toEntity(starRequest);
            user.linkStar(star);
            starRepository.saveAndFlush(star);

            applicationEventPublisher.publishEvent(
                    new StarCreatedEvent(
                            star.getId(),
                            star.getMessage(),
                            star.getX(),
                            star.getY()
                    )
            );

            log.info("Star created successfully. user={}, starId={}, x={}, y={}",
                    userEmail, star.getId(), star.getX(), star.getY());

            return StarMapper.toPointDTO(star);

        } catch (DataIntegrityViolationException ex) {
            log.warn("Star creation conflict for user={}. Requested coordinates may already be occupied.", userEmail, ex);
            throw new StarCreationException("star.creation.conflict");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StarCoordinatesDTO> getAllStarCoordinates(int page, int size) {
        log.info("Fetching star coordinates. page={}, size={}", page, size);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<StarCoordinatesDTO> result = this.starRepository.findAllStarCoordinates(pageRequest);

        log.info("Fetched star coordinates successfully. page={}, size={}, returnedElements={}, totalElements={}",
                page, size, result.getNumberOfElements(), result.getTotalElements());

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StarCoordinatesDTO> search(String keyword) {
        log.info("Star search requested. keyword={}", keyword);

       checkKeywordExists(keyword);

        List<StarCoordinatesDTO> list = this.starESRepository
                .findByMessageContaining(keyword)
                .stream()
                .map(StarDocumentMapper::toCoordinatesDto)
                .toList();

        if (list.isEmpty()) {
            log.warn("No stars found for keyword={}", keyword);
            throw new StarNotFoundException("star.search.not.found", keyword);
        }

        log.info("Star search completed successfully. keyword={}, resultCount={}", keyword, list.size());
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public StarMessageDTO getStarMessage(int x, int y) {
        log.info("Fetching star message by coordinates. x={}, y={}", x, y);

        StarMessageDTO starMessageDTO = starRepository.getMessageByXandY(x, y)
                .orElseThrow(() -> {
                    log.warn("Star message not found for coordinates. x={}, y={}", x, y);
                    return new StarNotFoundException("star.message.not.found", x, y);
                });

        log.info("Star message fetched successfully for coordinates. x={}, y={}", x, y);
        return starMessageDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public StarCoordinatesDTO getUserStar() {
        String email = getUserEmail();

        log.info("Fetching star for authenticated user={}", email);

        if (email == null) {
            log.warn("Authenticated user email is null while fetching user star");
            throw new UsernameNotFoundException("user.not.found");
        }

        StarCoordinatesDTO starCoordinatesDTO = starRepository.findUserStar(email)
                .orElseThrow(() -> {
                    log.warn("No star found for user={}", email);
                    return new StarNotFoundException("star.user.not.found", email);
                });

        log.info("User star fetched successfully. user={}", email);
        return starCoordinatesDTO;
    }

    private String getUserEmail() {
        Authentication authentication = Objects.requireNonNull(
                SecurityContextHolder.getContext().getAuthentication(),
                "Authentication must not be null"
        );

        String email = authentication.getName();
        log.debug("Authenticated user resolved as {}", email);

        return email;
    }
    private void checkKeywordExists(String keyword){
        if (keyword == null || keyword.isBlank()) {
            log.warn("Star search rejected because keyword is null or blank");
            throw new IllegalArgumentException("search.keyword.empty");
        }
    }
}
