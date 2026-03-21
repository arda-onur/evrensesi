package com.arda.evrensesi.UnitTest;

import com.arda.evrensesi.exception.customException.StarNotFoundException;
import com.arda.evrensesi.model.entity.Star;
import com.arda.evrensesi.repository.StarRepository;
import com.arda.evrensesi.service.impl.StarIndexStatusServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StarIndexStatusServiceTest {
    @Mock
    private StarRepository starRepository;

    @InjectMocks
    private StarIndexStatusServiceImpl starIndexStatusService;

    @Test
    void markIndexed_shouldMarkStarAsIndexedAndSave() {
        UUID starId = UUID.randomUUID();

        Star star = new Star();
        star.setId(starId);
        star.setEsIndexed(false);

        when(starRepository.findById(starId)).thenReturn(Optional.of(star));

        starIndexStatusService.markIndexed(starId);

        assertTrue(star.isEsIndexed());
        verify(starRepository).findById(starId);
        verify(starRepository).save(star);
    }
    @Test
    void markIndexed_shouldThrowStarNotFoundExceptionWhenStarDoesNotExist() {
        UUID starId = UUID.randomUUID();

        when(starRepository.findById(starId)).thenReturn(Optional.empty());

        assertThrows(
                StarNotFoundException.class,
                () -> starIndexStatusService.markIndexed(starId)
        );

        verify(starRepository).findById(starId);
    }
}
