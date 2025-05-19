package org.abrohamovich.service;

import org.abrohamovich.dto.AuthorDto;
import org.abrohamovich.dto.BookDto;
import org.abrohamovich.dto.CategoryDto;
import org.abrohamovich.dto.GenreDto;
import org.abrohamovich.entity.Book;
import org.abrohamovich.entity.Sex;
import org.abrohamovich.exceptions.BookAlreadyExistException;
import org.abrohamovich.exceptions.BookNotFoundException;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.mapper.BookMapper;
import org.abrohamovich.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceCRUDTest {

    @InjectMocks
    private BookServiceCRUD bookServiceCRUD;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    private Book book1;
    private BookDto bookDto1;

    @BeforeEach
    void setUp() {
        UUID isbn = UUID.randomUUID();
        book1 = Book.builder()
                .id(1L).title("Book One").isbn(isbn.toString()).language("English")
                .genres(Set.of()).categories(Set.of()).authors(Set.of())
                .build();

        bookDto1 = BookDto.builder()
                .id(1L).title("Book One").isbn(isbn.toString()).language("English")
                .genres(Set.of()).categories(Set.of()).authors(Set.of())
                .build();
    }

    @Test
    void save_ReturnsBookDto() {
        when(bookRepository.findByIsbn(bookDto1.getIsbn())).thenReturn(Optional.empty());
        when(bookMapper.toEntity(bookDto1)).thenReturn(book1);
        when(bookRepository.save(book1)).thenReturn(Optional.of(book1));
        when(bookMapper.toDto(book1)).thenReturn(bookDto1);

        BookDto result = bookServiceCRUD.save(bookDto1);

        assertNotNull(result);
        assertEquals(bookDto1.getId(), result.getId());
        verify(bookRepository).findByIsbn(bookDto1.getIsbn());
        verify(bookMapper).toEntity(bookDto1);
        verify(bookRepository).save(book1);
        verify(bookMapper).toDto(book1);
    }

    @Test
    void save_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookServiceCRUD.save(null));
        verify(bookRepository, never()).findByIsbn(anyString());
        verify(bookMapper, never()).toEntity(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void save_ThrowsBookAlreadyExistException() {
        when(bookRepository.findByIsbn(bookDto1.getIsbn())).thenReturn(Optional.of(book1));

        assertThrows(BookAlreadyExistException.class, () -> bookServiceCRUD.save(bookDto1));
        verify(bookRepository).findByIsbn(bookDto1.getIsbn());
        verify(bookMapper, never()).toEntity(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void save_ThrowsEntityException() {
        when(bookRepository.findByIsbn(bookDto1.getIsbn())).thenReturn(Optional.empty());
        when(bookMapper.toEntity(bookDto1)).thenReturn(book1);
        when(bookRepository.save(book1)).thenReturn(Optional.empty());

        assertThrows(EntityException.class, () -> bookServiceCRUD.save(bookDto1));
        verify(bookRepository).findByIsbn(bookDto1.getIsbn());
        verify(bookMapper).toEntity(bookDto1);
        verify(bookRepository).save(book1);
        verify(bookMapper, never()).toDto(any());
    }

    @Test
    void findById_ReturnsBookDto() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookMapper.toDto(book1)).thenReturn(bookDto1);

        BookDto result = bookServiceCRUD.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookRepository).findById(1L);
        verify(bookMapper).toDto(book1);
    }

    @Test
    void findById_ThrowsBookNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookServiceCRUD.findById(1L));
        verify(bookRepository).findById(1L);
        verify(bookMapper, never()).toDto(any());
    }

    @Test
    void findByIsbn_ReturnsBookDto() {
        when(bookRepository.findByIsbn(bookDto1.getIsbn())).thenReturn(Optional.of(book1));
        when(bookMapper.toDto(book1)).thenReturn(bookDto1);

        BookDto result = bookServiceCRUD.findByIsbn(bookDto1.getIsbn());

        assertNotNull(result);
        assertEquals(bookDto1.getId(), result.getId());
        verify(bookRepository).findByIsbn(bookDto1.getIsbn());
        verify(bookMapper).toDto(book1);
    }

    @Test
    void findByIsbn_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookServiceCRUD.findByIsbn(" "));
        verify(bookRepository, never()).findByIsbn(anyString());
    }

    @Test
    void findByIsbn_ThrowsBookNotFoundException() {
        when(bookRepository.findByIsbn(bookDto1.getIsbn())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookServiceCRUD.findByIsbn(bookDto1.getIsbn()));
        verify(bookRepository).findByIsbn(bookDto1.getIsbn());
        verify(bookMapper, never()).toDto(any());
    }

    @Test
    void findByAuthors_ReturnsBooks() {
        List<AuthorDto> authors = List.of(AuthorDto.builder().id(1L).sex(Sex.MALE).nationality("German").fullName("name").dateOfBirth(LocalDate.now()).build());
        when(bookRepository.findByAuthorIds(List.of(1L))).thenReturn(List.of(book1));
        when(bookMapper.toDto(book1)).thenReturn(bookDto1);

        List<BookDto> result = bookServiceCRUD.findByAuthors(authors);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookDto1.getId(), result.getFirst().getId());
        verify(bookRepository).findByAuthorIds(List.of(1L));
        verify(bookMapper).toDto(book1);
    }

    @Test
    void findByAuthors_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookServiceCRUD.findByAuthors(null));
        assertThrows(IllegalArgumentException.class, () -> bookServiceCRUD.findByAuthors(Collections.emptyList()));
        verify(bookRepository, never()).findByAuthorIds(any());
    }

    @Test
    void findByGenres_ReturnsBooks() {
        List<GenreDto> genres = List.of(GenreDto.builder().id(1L).name("name").description("desc").build());
        when(bookRepository.findByGenreIds(List.of(1L))).thenReturn(List.of(book1));
        when(bookMapper.toDto(book1)).thenReturn(bookDto1);

        List<BookDto> result = bookServiceCRUD.findByGenres(genres);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookDto1.getId(), result.getFirst().getId());
        verify(bookRepository).findByGenreIds(List.of(1L));
        verify(bookMapper).toDto(book1);
    }

    @Test
    void findByGenres_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookServiceCRUD.findByGenres(null));
        assertThrows(IllegalArgumentException.class, () -> bookServiceCRUD.findByGenres(Collections.emptyList()));
        verify(bookRepository, never()).findByGenreIds(any());
    }

    @Test
    void findByCategories_ReturnsBooks() {
        List<CategoryDto> categories = List.of(CategoryDto.builder().id(1L).name("name").description("desc").build());
        when(bookRepository.findByCategoryIds(List.of(1L))).thenReturn(List.of(book1));
        when(bookMapper.toDto(book1)).thenReturn(bookDto1);

        List<BookDto> result = bookServiceCRUD.findByCategories(categories);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookDto1.getId(), result.getFirst().getId());
        verify(bookRepository).findByCategoryIds(List.of(1L));
        verify(bookMapper).toDto(book1);
    }

    @Test
    void findByCategories_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookServiceCRUD.findByCategories(null));
        assertThrows(IllegalArgumentException.class, () -> bookServiceCRUD.findByCategories(Collections.emptyList()));
        verify(bookRepository, never()).findByCategoryIds(any());
    }

    @Test
    void findByLanguage_ReturnsBooks() {
        when(bookRepository.findByLanguage("English")).thenReturn(List.of(book1));
        when(bookMapper.toDto(book1)).thenReturn(bookDto1);

        List<BookDto> result = bookServiceCRUD.findByLanguage("English");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookDto1.getId(), result.getFirst().getId());
        verify(bookRepository).findByLanguage("English");
        verify(bookMapper).toDto(book1);
    }

    @Test
    void findByLanguage_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookServiceCRUD.findByLanguage(" "));
        verify(bookRepository, never()).findByLanguage(anyString());
    }

    @Test
    void findAll_ReturnsListOfBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(book1));
        when(bookMapper.toDto(book1)).thenReturn(bookDto1);

        List<BookDto> result = bookServiceCRUD.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookDto1.getId(), result.getFirst().getId());
        verify(bookRepository).findAll();
        verify(bookMapper).toDto(book1);
    }

    @Test
    void update_ReturnsUpdatedBookDto() {
        when(bookMapper.toEntity(bookDto1)).thenReturn(book1);
        when(bookRepository.update(book1)).thenReturn(Optional.of(book1));
        when(bookMapper.toDto(book1)).thenReturn(bookDto1);

        BookDto result = bookServiceCRUD.update(bookDto1);

        assertNotNull(result);
        assertEquals(bookDto1.getId(), result.getId());
        verify(bookMapper).toEntity(bookDto1);
        verify(bookRepository).update(book1);
        verify(bookMapper).toDto(book1);
    }

    @Test
    void update_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookServiceCRUD.update(null));
        verify(bookMapper, never()).toEntity(any());
        verify(bookRepository, never()).update(any());
    }

    @Test
    void update_ThrowsBookNotFoundException() {
        when(bookMapper.toEntity(bookDto1)).thenReturn(book1);
        when(bookRepository.update(book1)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookServiceCRUD.update(bookDto1));
        verify(bookMapper).toEntity(bookDto1);
        verify(bookRepository).update(book1);
        verify(bookMapper, never()).toDto(any());
    }

    @Test
    void delete_DeletesBook() {
        when(bookRepository.findById(bookDto1.getId())).thenReturn(Optional.of(book1));

        bookServiceCRUD.delete(bookDto1);

        verify(bookRepository).findById(bookDto1.getId());
        verify(bookRepository).deleteById(bookDto1.getId());
    }

    @Test
    void delete_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookServiceCRUD.delete(null));
        verify(bookRepository, never()).findById(anyLong());
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_ThrowsBookNotFoundException() {
        when(bookRepository.findById(bookDto1.getId())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookServiceCRUD.delete(bookDto1));
        verify(bookRepository).findById(bookDto1.getId());
        verify(bookRepository, never()).deleteById(anyLong());
    }
}