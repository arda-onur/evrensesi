package com.arda.evrensesi.service.impl;

import com.arda.evrensesi.dto.StarPointDTO;
import com.arda.evrensesi.entity.Star;
import com.arda.evrensesi.entity.User;
import com.arda.evrensesi.exception.StarAlreadyExistsException;
import com.arda.evrensesi.mapper.StarMapper;
import com.arda.evrensesi.repository.StarRepository;
import com.arda.evrensesi.repository.UserRepository;
import com.arda.evrensesi.request.StarRequest;
import com.arda.evrensesi.service.StarService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
@Service
public class StarServiceImpl implements StarService {
        private final StarRepository starRepository;
        private final UserRepository userRepository;

        public StarServiceImpl(StarRepository starRepository, UserRepository userRepository) {
            this.starRepository = starRepository;
            this.userRepository = userRepository;
        }

        @Transactional
        public StarPointDTO createStar(StarRequest starRequest){
            String userEmail = getUserEmail();
            if(this.starRepository.existsByUserEmail(userEmail))
                throw new StarAlreadyExistsException("star.already.exists", userEmail);

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("user.not.found"));

            Star star = StarMapper.toEntity(starRequest);
            user.linkStar(star);

            starRepository.save(star);

            return StarMapper.toPointDTO(star);
        }

    private String getUserEmail(){
        Authentication authentication = Objects.requireNonNull(
                SecurityContextHolder.getContext().getAuthentication(),"Authentication must not be null");
        return authentication.getName();
    }

}
