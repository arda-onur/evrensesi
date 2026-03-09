package com.arda.evrensesi;

import com.arda.evrensesi.dto.StarCoordinatesDTO;
import com.arda.evrensesi.entity.Star;
import com.arda.evrensesi.entity.User;
import com.arda.evrensesi.exception.customException.StarAlreadyExistsException;
import com.arda.evrensesi.exception.customException.StarCreationException;
import com.arda.evrensesi.repository.StarRepository;
import com.arda.evrensesi.repository.UserRepository;
import com.arda.evrensesi.request.StarRequest;
import com.arda.evrensesi.service.impl.StarServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StarServiceUnitTest {
    @Mock
    private StarRepository starRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StarServiceImpl starService;

    @BeforeEach
    void setUp() {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("arda@example.com", "password");

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Test
    void createStar_shouldReturnCoordinatesDTO_whenRequestIsValid() {
        StarRequest request = new StarRequest("hello star", 120, 450);

        User user = new User();
        user.setEmail("arda@example.com");

        when(starRepository.existsByUserEmail("arda@example.com")).thenReturn(false);
        when(userRepository.findByEmail("arda@example.com")).thenReturn(Optional.of(user));
        when(starRepository.save(any(Star.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StarCoordinatesDTO result = starService.createStar(request);

        assertNotNull(result);
        assertEquals(120, result.x());
        assertEquals(450, result.y());

        verify(starRepository).existsByUserEmail("arda@example.com");
        verify(userRepository).findByEmail("arda@example.com");
        verify(starRepository).save(any(Star.class));
    }

    @Test
    void createStar_shouldThrowStarAlreadyExistsException_whenUserAlreadyHasStar() {
        StarRequest request = new StarRequest("hello star", 120, 450);

        when(starRepository.existsByUserEmail("arda@example.com")).thenReturn(true);

        assertThrows(StarAlreadyExistsException.class, () -> starService.createStar(request));

        verify(starRepository).existsByUserEmail("arda@example.com");
        verify(userRepository, never()).findByEmail(anyString());
        verify(starRepository, never()).save(any());
    }

    @Test
    void createStar_shouldThrowUsernameNotFoundException_whenUserDoesNotExist() {
        StarRequest request = new StarRequest("hello star", 120, 450);

        when(starRepository.existsByUserEmail("arda@example.com")).thenReturn(false);
        when(userRepository.findByEmail("arda@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> starService.createStar(request));

        verify(starRepository).existsByUserEmail("arda@example.com");
        verify(userRepository).findByEmail("arda@example.com");
        verify(starRepository, never()).save(any());
    }

    @Test
    void createStar_shouldThrowStarCreationException_whenSaveFailsWithDataIntegrityViolation() {
        StarRequest request = new StarRequest("hello star", 120, 450);

        User user = new User();
        user.setEmail("arda@example.com");

        when(starRepository.existsByUserEmail("arda@example.com")).thenReturn(false);
        when(userRepository.findByEmail("arda@example.com")).thenReturn(Optional.of(user));
        when(starRepository.save(any(Star.class)))
                .thenThrow(new DataIntegrityViolationException("constraint violation"));

        assertThrows(StarCreationException.class, () -> starService.createStar(request));

        verify(starRepository).save(any(Star.class));
    }

    @Test
    void getAllStarPoints_shouldReturnPageOfCoordinates() {
        int page = 0;
        int size = 2;

        List<StarCoordinatesDTO> content = List.of(new StarCoordinatesDTO(100, 200),
                                                   new StarCoordinatesDTO(300, 400));

        Pageable pageable = PageRequest.of(page, size);
        Page<StarCoordinatesDTO> expectedPage = new PageImpl<>(content, pageable, content.size());

        when(starRepository.findAllStarCoordinates(pageable)).thenReturn(expectedPage);

        Page<StarCoordinatesDTO> result = starService.getAllStarPoints(page, size);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(100, result.getContent().get(0).x());
        assertEquals(200, result.getContent().get(0).y());

        verify(starRepository).findAllStarCoordinates(pageable);
    }

}
