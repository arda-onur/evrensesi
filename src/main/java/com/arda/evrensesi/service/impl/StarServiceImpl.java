package com.arda.evrensesi.service.impl;

import com.arda.evrensesi.dto.StarCoordinatesDTO;
import com.arda.evrensesi.event.StarCreatedEvent;
import com.arda.evrensesi.mapper.search.StarDocumentMapper;
import com.arda.evrensesi.model.entity.Star;
import com.arda.evrensesi.model.entity.User;
import com.arda.evrensesi.exception.customException.StarAlreadyExistsException;
import com.arda.evrensesi.exception.customException.StarCreationException;
import com.arda.evrensesi.mapper.api.StarMapper;
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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class StarServiceImpl implements StarService {
        private final StarRepository starRepository;
        private final UserRepository userRepository;
        private final StarESRepository starESRepository;
        private final ApplicationEventPublisher applicationEventPublisher;

        public StarServiceImpl(StarRepository starRepository,
                               UserRepository userRepository,
                               StarESRepository starESRepository, ApplicationEventPublisher applicationEventPublisher) {
            this.starRepository = starRepository;
            this.userRepository = userRepository;
            this.starESRepository = starESRepository;
            this.applicationEventPublisher = applicationEventPublisher;
        }

        @Transactional
        public StarCoordinatesDTO createStar(StarRequest starRequest){

            String userEmail = getUserEmail();

            log.info("Star creation requested by user: {}", userEmail);

            if(this.starRepository.existsByUserEmail(userEmail))
                throw new StarAlreadyExistsException("star.already.exists", userEmail);

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("star.creation.must.be.login"));
            try {
                Star star = StarMapper.toEntity(starRequest);
                user.linkStar(star);
                starRepository.saveAndFlush(star);

                applicationEventPublisher.publishEvent(new StarCreatedEvent(star.getId(),
                                                                            star.getMessage(),
                                                                            star.getX(),
                                                                            star.getY()));

                log.info("Star created successfully for user: {} at coordinates ({},{})",
                        userEmail, star.getX(), star.getY());

                return StarMapper.toPointDTO(star);
            } catch (DataIntegrityViolationException ex) {
                throw new StarCreationException("star.creation.conflict");
            }
        }

    @Override
    @Transactional(readOnly = true)
    public Page<StarCoordinatesDTO> getAllStarCoordinates(int page, int size) {

        log.info("Fetching star coordinates. page={}, size={}", page, size);

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<StarCoordinatesDTO> result = this.starRepository.findAllStarCoordinates(pageRequest);

        log.info("Fetched {} star coordinates", result.getNumberOfElements());

        return result;
    }

    public List<StarCoordinatesDTO> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return Collections.emptyList();
        }
            List<StarCoordinatesDTO> list = this.starESRepository
                    .findByMessageContaining(keyword)
                    .stream()
                    .map(StarDocumentMapper::toCoordinatesDto)
                    .toList();

            return list;
    }

    public StarCoordinatesDTO getUserStar() {
        String email = getUserEmail();

        if (email == null) {
            throw new IllegalStateException("User is not authenticated");
        }

        return starRepository.findUserStar(email)
                .orElseThrow(() -> new RuntimeException("Star not found for user: " + email));
    }

    private String getUserEmail(){
        Authentication authentication = Objects.requireNonNull(
                SecurityContextHolder.getContext().getAuthentication(),"Authentication must not be null");
        return authentication.getName();
    }

}
