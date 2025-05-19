package org.abrohamovich.service;

import org.abrohamovich.dto.AuthorDto;
import org.abrohamovich.entity.Author;
import org.abrohamovich.entity.Sex;
import org.abrohamovich.exceptions.AuthorNotFoundException;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.mapper.AuthorMapper;
import org.abrohamovich.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceCRUDTest {
    @InjectMocks
    private AuthorServiceCRUD authorServiceCRUD;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private AuthorMapper authorMapper;
    private Author author1;
    private AuthorDto authorDto1;
    private Author author2;
    private AuthorDto authorDto2;
    private Author author3;
    private AuthorDto authorDto3;

    @BeforeEach
    void setUp() {
        author1 = Author.builder()
                .id(1L)
                .fullName("Ernest Hemingway")
                .dateOfBirth(LocalDate.of(1899, 7, 21))
                .nationality("American")
                .sex(Sex.MALE)
                .build();
        author2 = Author.builder()
                .id(2L)
                .fullName("George Orwell")
                .dateOfBirth(LocalDate.of(1903, 6, 25))
                .nationality("British")
                .sex(Sex.MALE)
                .build();
        author3 = Author.builder()
                .id(3L)
                .fullName("Virginia Woolf")
                .dateOfBirth(LocalDate.of(1882, 1, 25))
                .nationality("British")
                .sex(Sex.FEMALE)
                .build();
        authorDto1 = AuthorDto.builder()
                .id(1L)
                .fullName("Ernest Hemingway")
                .dateOfBirth(LocalDate.of(1899, 7, 21))
                .nationality("American")
                .sex(Sex.MALE)
                .build();
        authorDto2 = AuthorDto.builder()
                .id(2L)
                .fullName("George Orwell")
                .dateOfBirth(LocalDate.of(1903, 6, 25))
                .nationality("British")
                .sex(Sex.MALE)
                .build();
        authorDto3 = AuthorDto.builder()
                .id(3L)
                .fullName("Virginia Woolf")
                .dateOfBirth(LocalDate.of(1882, 1, 25))
                .nationality("British")
                .sex(Sex.FEMALE)
                .build();
    }

    @Test
    void save_ReturnsSavedAuthorDto() {
        when(authorMapper.toEntity(authorDto1)).thenReturn(author1);
        when(authorRepository.save(author1)).thenReturn(Optional.of(author1));
        when(authorMapper.toDto(author1)).thenReturn(authorDto1);

        AuthorDto result = authorServiceCRUD.save(authorDto1);

        assertNotNull(result);
        assertEquals(authorDto1, result);
        verify(authorMapper).toEntity(authorDto1);
        verify(authorRepository).save(author1);
        verify(authorMapper).toDto(author1);
    }

    @Test
    void save_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> authorServiceCRUD.save(null));
        verify(authorMapper, never()).toEntity(any());
        verify(authorRepository, never()).save(any());
    }

    @Test
    void save_ThrowsEntityException() {
        when(authorMapper.toEntity(authorDto1)).thenReturn(author1);
        when(authorRepository.save(author1)).thenReturn(Optional.empty());

        assertThrows(EntityException.class, () -> authorServiceCRUD.save(authorDto1));
        verify(authorMapper).toEntity(authorDto1);
        verify(authorRepository).save(author1);
        verify(authorMapper, never()).toDto(any());
    }

    @Test
    void findById_ReturnsAuthorDto() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author1));
        when(authorMapper.toDto(author1)).thenReturn(authorDto1);

        AuthorDto result = authorServiceCRUD.findById(1L);

        assertNotNull(result);
        assertEquals(authorDto1, result);
        verify(authorRepository).findById(1L);
        verify(authorMapper).toDto(author1);
    }

    @Test
    void findById_ThrowsAuthorNotFoundException() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> authorServiceCRUD.findById(99L));
        verify(authorRepository).findById(99L);
        verify(authorMapper, never()).toDto(any());
    }

    @Test
    void findBySex_ReturnsAuthorDtoList() {
        when(authorRepository.findBySex(Sex.MALE)).thenReturn(List.of(author1, author2));
        when(authorMapper.toDto(author1)).thenReturn(authorDto1);
        when(authorMapper.toDto(author2)).thenReturn(authorDto2);

        List<AuthorDto> result = authorServiceCRUD.findBySex(Sex.MALE);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(authorDto1));
        assertTrue(result.contains(authorDto2));
        verify(authorRepository).findBySex(Sex.MALE);
        verify(authorMapper).toDto(author1);
        verify(authorMapper).toDto(author2);
    }

    @Test
    void findBySex_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> authorServiceCRUD.findBySex(null));
        verify(authorRepository, never()).findBySex(any());
    }

    @Test
    void findByFullName_ReturnsAuthorDtoList() {
        when(authorRepository.findByFullName("George Orwell")).thenReturn(List.of(author2));
        when(authorMapper.toDto(author2)).thenReturn(authorDto2);

        List<AuthorDto> result = authorServiceCRUD.findByFullName("George Orwell");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(authorDto2, result.getFirst());
        verify(authorRepository).findByFullName("George Orwell");
        verify(authorMapper).toDto(author2);
    }

    @Test
    void findByFullName_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> authorServiceCRUD.findByFullName("  "));
        verify(authorRepository, never()).findByFullName(anyString());
    }

    @Test
    void findAll_ReturnsAllAuthorDtos() {
        when(authorRepository.findAll()).thenReturn(List.of(author1, author2, author3));
        when(authorMapper.toDto(author1)).thenReturn(authorDto1);
        when(authorMapper.toDto(author2)).thenReturn(authorDto2);
        when(authorMapper.toDto(author3)).thenReturn(authorDto3);

        List<AuthorDto> result = authorServiceCRUD.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(authorDto1));
        assertTrue(result.contains(authorDto2));
        assertTrue(result.contains(authorDto3));
        verify(authorRepository).findAll();
        verify(authorMapper).toDto(author1);
        verify(authorMapper).toDto(author2);
        verify(authorMapper).toDto(author3);
    }

    @Test
    void update_ReturnsUpdatedAuthorDto() {
        when(authorMapper.toEntity(authorDto2)).thenReturn(author2);
        when(authorRepository.update(author2)).thenReturn(Optional.of(author2));
        when(authorMapper.toDto(author2)).thenReturn(authorDto2);

        AuthorDto result = authorServiceCRUD.update(authorDto2);

        assertNotNull(result);
        assertEquals(authorDto2, result);
        verify(authorMapper).toEntity(authorDto2);
        verify(authorRepository).update(author2);
        verify(authorMapper).toDto(author2);
    }

    @Test
    void update_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> authorServiceCRUD.update(null));
        verify(authorMapper, never()).toEntity(any());
        verify(authorRepository, never()).update(any());
    }

    @Test
    void update_ThrowsAuthorNotFoundException() {
        when(authorMapper.toEntity(authorDto3)).thenReturn(author3);
        when(authorRepository.update(author3)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> authorServiceCRUD.update(authorDto3));
        verify(authorMapper).toEntity(authorDto3);
        verify(authorRepository).update(author3);
        verify(authorMapper, never()).toDto(any());
    }

    @Test
    void delete_DeletesAuthor() {
        when(authorRepository.findById(3L)).thenReturn(Optional.of(author3));

        authorServiceCRUD.delete(authorDto3);

        verify(authorRepository).findById(3L);
        verify(authorRepository).deleteById(3L);
    }

    @Test
    void delete_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> authorServiceCRUD.delete(null));
        verify(authorRepository, never()).findById(anyLong());
        verify(authorRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_ThrowsAuthorNotFoundException() {
        when(authorRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> authorServiceCRUD.delete(authorDto3));
        verify(authorRepository).findById(3L);
        verify(authorRepository, never()).deleteById(anyLong());
    }
}