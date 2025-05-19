package org.abrohamovich.service;

import org.abrohamovich.dto.GenreDto;
import org.abrohamovich.entity.Genre;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.exceptions.GenreAlreadyExistException;
import org.abrohamovich.exceptions.GenreNotFoundException;
import org.abrohamovich.mapper.GenreMapper;
import org.abrohamovich.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GenreServiceCRUDTest {
    @InjectMocks
    private GenreServiceCRUD genreServiceCRUD;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private GenreMapper genreMapper;
    private Genre genre1;
    private Genre genre2;
    private Genre genre3;
    private GenreDto genreDto1;
    private GenreDto genreDto2;
    private GenreDto genreDto3;

    @BeforeEach
    void setUp() {
        genre1 = Genre.builder().id(1L).name("Mystery").description("Stories centered around solving a crime or puzzle").build();
        genre2 = Genre.builder().id(2L).name("Fantasy").description("Tales featuring magical or supernatural elements").build();
        genre3 = Genre.builder().id(3L).name("Biography").description("Narratives detailing the life of a real person").build();
        genreDto1 = GenreDto.builder().id(1L).name("Mystery").description("Stories centered around solving a crime or puzzle").build();
        genreDto2 = GenreDto.builder().id(2L).name("Fantasy").description("Tales featuring magical or supernatural elements").build();
        genreDto3 = GenreDto.builder().id(3L).name("Biography").description("Narratives detailing the life of a real person").build();
    }

    @Test
    void save_ReturnsGenreDto() {
        when(genreRepository.findByName(genreDto1.getName())).thenReturn(Optional.empty());
        when(genreMapper.toEntity(genreDto1)).thenReturn(genre1);
        when(genreRepository.save(genre1)).thenReturn(Optional.of(genre1));
        when(genreMapper.toDto(genre1)).thenReturn(genreDto1);

        GenreDto result = genreServiceCRUD.save(genreDto1);

        assertNotNull(result);
        assertEquals(genreDto1, result);
        verify(genreRepository).findByName(genreDto1.getName());
        verify(genreMapper).toEntity(genreDto1);
        verify(genreRepository).save(genre1);
        verify(genreMapper).toDto(genre1);
    }

    @Test
    void save_ThrowsGenreAlreadyExistException() {
        when(genreRepository.findByName(genreDto1.getName())).thenReturn(Optional.of(genre1));

        assertThrows(GenreAlreadyExistException.class, () -> genreServiceCRUD.save(genreDto1));
        verify(genreRepository).findByName(genreDto1.getName());
        verify(genreMapper, never()).toEntity(any());
        verify(genreRepository, never()).save(any());
    }

    @Test
    void save_ThrowsEntityException() {
        when(genreRepository.findByName(genreDto1.getName())).thenReturn(Optional.empty());
        when(genreMapper.toEntity(genreDto1)).thenReturn(genre1);
        when(genreRepository.save(genre1)).thenReturn(Optional.empty());

        assertThrows(EntityException.class, () -> genreServiceCRUD.save(genreDto1));
        verify(genreRepository).findByName(genreDto1.getName());
        verify(genreMapper).toEntity(genreDto1);
        verify(genreRepository).save(genre1);
        verify(genreMapper, never()).toDto(any());
    }

    @Test
    void save_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> genreServiceCRUD.save(null));
        verify(genreRepository, never()).findByName(anyString());
        verify(genreMapper, never()).toEntity(any());
        verify(genreRepository, never()).save(any());
    }

    @Test
    void findById_ReturnsGenreDto() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre1));
        when(genreMapper.toDto(genre1)).thenReturn(genreDto1);

        GenreDto result = genreServiceCRUD.findById(1L);

        assertNotNull(result);
        assertEquals(genreDto1, result);
        verify(genreRepository).findById(1L);
        verify(genreMapper).toDto(genre1);
    }

    @Test
    void findById_ThrowsGenreNotFoundException() {
        when(genreRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GenreNotFoundException.class, () -> genreServiceCRUD.findById(1L));
        verify(genreRepository).findById(1L);
        verify(genreMapper, never()).toDto(any());
    }

    @Test
    void findByName_ReturnsGenreDto() {
        when(genreRepository.findByName("Mystery")).thenReturn(Optional.of(genre1));
        when(genreMapper.toDto(genre1)).thenReturn(genreDto1);

        GenreDto result = genreServiceCRUD.findByName("Mystery");

        assertNotNull(result);
        assertEquals(genreDto1, result);
        verify(genreRepository).findByName("Mystery");
        verify(genreMapper).toDto(genre1);
    }

    @Test
    void findByName_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> genreServiceCRUD.findByName(" "));
        verify(genreRepository, never()).findByName(anyString());
    }

    @Test
    void findByName_ThrowsGenreNotFoundException() {
        when(genreRepository.findByName("Horror")).thenReturn(Optional.empty());

        assertThrows(GenreNotFoundException.class, () -> genreServiceCRUD.findByName("Horror"));
        verify(genreRepository).findByName("Horror");
        verify(genreMapper, never()).toDto(any());
    }

    @Test
    void findAll_ReturnsGenreDtoList() {
        List<Genre> genres = List.of(genre1, genre2, genre3);
        when(genreRepository.findAll()).thenReturn(genres);
        when(genreMapper.toDto(genre1)).thenReturn(genreDto1);
        when(genreMapper.toDto(genre2)).thenReturn(genreDto2);
        when(genreMapper.toDto(genre3)).thenReturn(genreDto3);

        List<GenreDto> result = genreServiceCRUD.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(genreDto1));
        assertTrue(result.contains(genreDto2));
        assertTrue(result.contains(genreDto3));
        verify(genreRepository).findAll();
        verify(genreMapper).toDto(genre1);
        verify(genreMapper).toDto(genre2);
        verify(genreMapper).toDto(genre3);
    }

    @Test
    void update_ReturnsUpdatedGenreDto() {
        when(genreMapper.toEntity(genreDto2)).thenReturn(genre2);
        when(genreRepository.update(genre2)).thenReturn(Optional.of(genre2));
        when(genreMapper.toDto(genre2)).thenReturn(genreDto2);

        GenreDto result = genreServiceCRUD.update(genreDto2);

        assertNotNull(result);
        assertEquals(genreDto2, result);
        verify(genreMapper).toEntity(genreDto2);
        verify(genreRepository).update(genre2);
        verify(genreMapper).toDto(genre2);
    }

    @Test
    void update_ThrowsGenreNotFoundException() {
        when(genreMapper.toEntity(genreDto2)).thenReturn(genre2);
        when(genreRepository.update(genre2)).thenReturn(Optional.empty());

        assertThrows(GenreNotFoundException.class, () -> genreServiceCRUD.update(genreDto2));
        verify(genreMapper).toEntity(genreDto2);
        verify(genreRepository).update(genre2);
        verify(genreMapper, never()).toDto(any());
    }

    @Test
    void update_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> genreServiceCRUD.update(null));
        verify(genreMapper, never()).toEntity(any());
        verify(genreRepository, never()).update(any());
    }

    @Test
    void delete_DeletesGenre() {
        when(genreRepository.findById(genreDto3.getId())).thenReturn(Optional.of(genre3));

        genreServiceCRUD.delete(genreDto3);

        verify(genreRepository).findById(genreDto3.getId());
        verify(genreRepository).deleteById(genreDto3.getId());
    }

    @Test
    void delete_ThrowsGenreNotFoundException() {
        when(genreRepository.findById(genreDto3.getId())).thenReturn(Optional.empty());

        assertThrows(GenreNotFoundException.class, () -> genreServiceCRUD.delete(genreDto3));
        verify(genreRepository).findById(genreDto3.getId());
        verify(genreRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> genreServiceCRUD.delete(null));
        verify(genreRepository, never()).findById(anyLong());
        verify(genreRepository, never()).deleteById(anyLong());
    }
}