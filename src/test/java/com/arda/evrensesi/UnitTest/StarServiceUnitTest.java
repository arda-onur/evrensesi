package com.arda.evrensesi.UnitTest;

import com.arda.evrensesi.dto.StarCoordinatesDTO;
import com.arda.evrensesi.dto.StarMessageDTO;
import com.arda.evrensesi.event.StarCreatedEvent;
import com.arda.evrensesi.exception.customException.StarAlreadyExistsException;
import com.arda.evrensesi.exception.customException.StarNotFoundException;
import com.arda.evrensesi.model.document.StarDocument;
import com.arda.evrensesi.model.entity.Star;
import com.arda.evrensesi.model.entity.User;
import com.arda.evrensesi.repository.StarESRepository;
import com.arda.evrensesi.repository.StarRepository;
import com.arda.evrensesi.repository.UserRepository;
import com.arda.evrensesi.request.StarRequest;
import com.arda.evrensesi.service.impl.StarServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
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

    @Mock
    private StarESRepository starESRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private StarServiceImpl starService;

    @Test
    public void createStar_shouldCreateStarSuccessfully(){
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("arda@mail.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        StarRequest request = new StarRequest("Merhaba yıldız", 100, 200);

        User user = new User();
        user.setEmail("arda@mail.com");

        when(starRepository.existsByUserEmail("arda@mail.com")).thenReturn(false);
        when(userRepository.findByEmail("arda@mail.com")).thenReturn(Optional.of(user));

        starService.createStar(request);

        ArgumentCaptor<Star> starCaptor = ArgumentCaptor.forClass(Star.class);
        ArgumentCaptor<StarCreatedEvent> eventCaptor = ArgumentCaptor.forClass(StarCreatedEvent.class);


        verify(starRepository).existsByUserEmail("arda@mail.com");
        verify(userRepository).findByEmail("arda@mail.com");

        verify(starRepository).saveAndFlush(starCaptor.capture());

        Star savedStar = starCaptor.getValue();

        assertNotNull(savedStar);
        assertEquals("Merhaba yıldız", savedStar.getMessage());
        assertEquals(100, savedStar.getX());
        assertEquals(200, savedStar.getY());
    }
    @Test
    public void createStar_shouldThrowStarAlreadyExistsExceptionWhenUserAlreadyHasStar(){
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("arda@mail.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        StarRequest request = new StarRequest("Merhaba yıldız", 100, 200);

        User user = new User();
        user.setEmail("arda@mail.com");

        when(starRepository.existsByUserEmail(user.getEmail())).thenReturn(true);

        assertThrows(StarAlreadyExistsException.class, () -> starService.createStar(request));
        verify(starRepository).existsByUserEmail("arda@mail.com");
        verify(userRepository, never()).findByEmail(anyString());
        verify(starRepository, never()).saveAndFlush(any(Star.class));
        verify(applicationEventPublisher, never()).publishEvent(any());
    }
    @Test
    public void createStar_shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist(){
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("arda@mail.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);


        StarRequest request = new StarRequest("Merhaba yıldız", 100, 200);


        when(starRepository.existsByUserEmail("arda@mail.com")).thenReturn(false);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> starService.createStar(request));

        verify(starRepository, never()).saveAndFlush(any());
        verify(applicationEventPublisher,never()).publishEvent(any());
    }
    @Test
    public void getAllStarCoordinates_shouldReturnPagedCoordinates(){

        int page = 0;
        int size = 5;

        List<StarCoordinatesDTO> content = List.of(
                new StarCoordinatesDTO(10, 20),
                new StarCoordinatesDTO(30, 40)
        );

        Page<StarCoordinatesDTO> expectedPage = new PageImpl<>(content);

        when(starRepository.findAllStarCoordinates(any()))
                .thenReturn(expectedPage);

        Page<StarCoordinatesDTO> result = starService.getAllStarCoordinates(page, size);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(10, result.getContent().get(0).x());

    }
    @Test
    void search_shouldReturnCoordinatesWhenKeywordExists() {
        String keyword = "merhaba";

        StarDocument doc1 = new StarDocument();
        doc1.setMessage("merhaba dünya");
        doc1.setX(10);
        doc1.setY(20);

        StarDocument doc2 = new StarDocument();
        doc2.setMessage("merhaba again");
        doc2.setX(30);
        doc2.setY(40);

        when(starESRepository.findByMessageContaining(any())).thenReturn(
                List.of(doc1,doc2));

        List<StarCoordinatesDTO> result = starService.search(keyword);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(10, result.get(0).x());
        assertEquals(20, result.get(0).y());
        assertEquals(30, result.get(1).x());
        assertEquals(40, result.get(1).y());

        verify(starESRepository).findByMessageContaining(keyword);
    }
    @Test
    public void search_shouldThrowIllegalArgumentExceptionWhenKeywordIsBlank(){
        String keyword = "";

        assertThrows(IllegalArgumentException.class, () -> starService.search(keyword));

        verify(starESRepository, never()).findByMessageContaining(anyString());
    }

    @Test
    public void search_shouldThrowStarNotFoundExceptionWhenNoResultExists(){
        String keyword = "nothing";

        when(starESRepository.findByMessageContaining(keyword)).thenReturn(List.of());

        assertThrows(
                StarNotFoundException.class,
                () -> starService.search(keyword)
        );

        verify(starESRepository).findByMessageContaining(keyword);
    }

    @Test
    public void getStarMessage_shouldReturnMessageWhenCoordinatesExist(){
        int x = 200, y=333;

        StarMessageDTO expected = new StarMessageDTO("merhaba dünya");

        when(starRepository.getMessageByXandY(x,y))
                .thenReturn(Optional.of(expected));


        StarMessageDTO result = starService.getStarMessage(x,y);

        assertNotNull(result);
        assertEquals(expected.message(),result.message());

        verify(starRepository).getMessageByXandY(x,y);

    }
   @Test
   public void getStarMessage_shouldThrowStarNotFoundExceptionWhenCoordinatesDoNotExist(){
       when(starRepository.getMessageByXandY(1,2))
               .thenThrow(new StarNotFoundException("star not found"));


       assertThrows(StarNotFoundException.class, () -> starService.getStarMessage(1,2));
   }
   @Test
   public void getUserStar_shouldReturnAuthenticatedUsersStar(){
       Authentication authentication = mock(Authentication.class);
       when(authentication.getName()).thenReturn("ardaonur@gmail");
       SecurityContextHolder.getContext().setAuthentication(authentication);


       when(starRepository.findUserStar(any())).thenReturn(Optional.of(new StarCoordinatesDTO(1,2)));

       StarCoordinatesDTO result = starService.getUserStar();

       assertEquals(1, result.x());
       assertEquals(2, result.y());

       verify(starRepository).findUserStar("ardaonur@gmail");
    }

   @Test
    public void  getUserStar_shouldThrowStarNotFoundExceptionWhenUserHasNoStar(){
       Authentication authentication = mock(Authentication.class);
       when(authentication.getName()).thenReturn("ardaonur@gmail");
       SecurityContextHolder.getContext().setAuthentication(authentication);

       when(starRepository.findUserStar("ardaonur@gmail"))
               .thenReturn(Optional.empty());

       assertThrows(StarNotFoundException.class, () -> starService.getUserStar());

       verify(starRepository).findUserStar("ardaonur@gmail");       assertThrows(StarNotFoundException.class , () -> starService.getUserStar());
    }

    @Test
    public void getUserStar_shouldThrowUsernameNotFoundExceptionWhenEmailIsNull(){
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThrows(UsernameNotFoundException.class, () -> starService.getUserStar());

        verify(starRepository, never()).findUserStar(anyString());

    }
}
