package org.abrohamovich.service;

import org.abrohamovich.dto.BookDto;
import org.abrohamovich.dto.BookInstanceDto;
import org.abrohamovich.dto.PublisherDto;
import org.abrohamovich.entity.*;
import org.abrohamovich.exceptions.BookInstanceNotFoundException;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.mapper.BookInstanceMapper;
import org.abrohamovich.repository.BookInstanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookInstanceServiceCRUDTest {
    @InjectMocks
    private BookInstanceServiceCRUD bookInstanceServiceCRUD;
    @Mock
    private BookInstanceRepository bookInstanceRepository;
    @Mock
    private BookInstanceMapper bookInstanceMapper;

    private BookInstance bookInstance;
    private BookInstanceDto bookInstanceDto;

    @BeforeEach
    void setUp() {
        bookInstance = BookInstance.builder()
                .id(1L)
                .book(mock(Book.class))
                .publisher(mock(Publisher.class))
                .numberOfPages(130)
                .receiptDate(LocalDate.now())
                .status(Status.AVAILABLE)
                .format(Format.PAPERBACK)
                .build();
        bookInstanceDto = BookInstanceDto.builder()
                .id(1L)
                .book(mock(BookDto.class))
                .publisher(mock(PublisherDto.class))
                .numberOfPages(130)
                .receiptDate(LocalDate.now())
                .status(Status.AVAILABLE)
                .format(Format.PAPERBACK)
                .build();
    }

    @Test
    void save_ReturnsSavedBookInstanceDto() {
        when(bookInstanceMapper.toEntity(bookInstanceDto)).thenReturn(bookInstance);
        when(bookInstanceRepository.save(bookInstance)).thenReturn(Optional.of(bookInstance));
        when(bookInstanceMapper.toDto(bookInstance)).thenReturn(bookInstanceDto);

        BookInstanceDto result = bookInstanceServiceCRUD.save(bookInstanceDto);

        assertNotNull(result);
        assertEquals(bookInstanceDto.getId(), result.getId());
        verify(bookInstanceMapper).toEntity(bookInstanceDto);
        verify(bookInstanceRepository).save(bookInstance);
        verify(bookInstanceMapper).toDto(bookInstance);
    }

    @Test
    void save_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookInstanceServiceCRUD.save(null));
        verify(bookInstanceMapper, never()).toEntity(any());
        verify(bookInstanceRepository, never()).save(any());
    }

    @Test
    void save_ThrowsEntityException() {
        when(bookInstanceMapper.toEntity(bookInstanceDto)).thenReturn(bookInstance);
        when(bookInstanceRepository.save(bookInstance)).thenReturn(Optional.empty());

        assertThrows(EntityException.class, () -> bookInstanceServiceCRUD.save(bookInstanceDto));
        verify(bookInstanceMapper).toEntity(bookInstanceDto);
        verify(bookInstanceRepository).save(bookInstance);
        verify(bookInstanceMapper, never()).toDto(any());
    }

    @Test
    void findById_ReturnsBookInstanceDto() {
        when(bookInstanceRepository.findById(1L)).thenReturn(Optional.of(bookInstance));
        when(bookInstanceMapper.toDto(bookInstance)).thenReturn(bookInstanceDto);

        BookInstanceDto result = bookInstanceServiceCRUD.findById(1L);

        assertNotNull(result);
        assertEquals(bookInstanceDto.getId(), result.getId());
        verify(bookInstanceRepository).findById(1L);
        verify(bookInstanceMapper).toDto(bookInstance);
    }

    @Test
    void findById_ThrowsBookInstanceNotFoundException() {
        when(bookInstanceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookInstanceNotFoundException.class, () -> bookInstanceServiceCRUD.findById(1L));
        verify(bookInstanceRepository).findById(1L);
        verify(bookInstanceMapper, never()).toDto(any());
    }

    @Test
    void findByBook_ReturnsListOfBookInstanceDto() {
        BookDto bookDto = BookDto.builder().id(1L).title("wqe").isbn("qwe").language("english").build();
        when(bookInstanceRepository.findByBookId(1L)).thenReturn(List.of(bookInstance));
        when(bookInstanceMapper.toDto(bookInstance)).thenReturn(bookInstanceDto);

        List<BookInstanceDto> result = bookInstanceServiceCRUD.findByBook(bookDto);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookInstanceDto.getId(), result.getFirst().getId());
        verify(bookInstanceRepository).findByBookId(1L);
        verify(bookInstanceMapper).toDto(bookInstance);
    }

    @Test
    void findByBook_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookInstanceServiceCRUD.findByBook(null));
        verify(bookInstanceRepository, never()).findByBookId(anyLong());
    }

    @Test
    void findByPublisher_ReturnsListOfBookInstanceDto() {
        PublisherDto publisherDto = PublisherDto.builder().id(1L).name("smth").foundationDate(LocalDate.now()).build();
        when(bookInstanceRepository.findByPublisherId(1L)).thenReturn(List.of(bookInstance));
        when(bookInstanceMapper.toDto(bookInstance)).thenReturn(bookInstanceDto);

        List<BookInstanceDto> result = bookInstanceServiceCRUD.findByPublisher(publisherDto);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookInstanceDto.getId(), result.getFirst().getId());
        verify(bookInstanceRepository).findByPublisherId(1L);
        verify(bookInstanceMapper).toDto(bookInstance);
    }

    @Test
    void findByPublisher_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookInstanceServiceCRUD.findByPublisher(null));
        verify(bookInstanceRepository, never()).findByPublisherId(anyLong());
    }

    @Test
    void findByStatus_ReturnsListOfBookInstanceDto() {
        when(bookInstanceRepository.findByStatus(Status.AVAILABLE)).thenReturn(List.of(bookInstance));
        when(bookInstanceMapper.toDto(bookInstance)).thenReturn(bookInstanceDto);

        List<BookInstanceDto> result = bookInstanceServiceCRUD.findByStatus(Status.AVAILABLE);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookInstanceDto.getId(), result.getFirst().getId());
        verify(bookInstanceRepository).findByStatus(Status.AVAILABLE);
        verify(bookInstanceMapper).toDto(bookInstance);
    }

    @Test
    void findByStatus_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookInstanceServiceCRUD.findByStatus(null));
        verify(bookInstanceRepository, never()).findByStatus(any());
    }

    @Test
    void findByFormat_ReturnsListOfBookInstanceDto() {
        when(bookInstanceRepository.findByFormat(Format.PAPERBACK)).thenReturn(List.of(bookInstance));
        when(bookInstanceMapper.toDto(bookInstance)).thenReturn(bookInstanceDto);

        List<BookInstanceDto> result = bookInstanceServiceCRUD.findByFormat(Format.PAPERBACK);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookInstanceDto.getId(), result.getFirst().getId());
        verify(bookInstanceRepository).findByFormat(Format.PAPERBACK);
        verify(bookInstanceMapper).toDto(bookInstance);
    }

    @Test
    void findByFormat_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookInstanceServiceCRUD.findByFormat(null));
        verify(bookInstanceRepository, never()).findByFormat(any());
    }

    @Test
    void findAll_ReturnsListOfBookInstanceDto() {
        when(bookInstanceRepository.findAll()).thenReturn(List.of(bookInstance));
        when(bookInstanceMapper.toDto(bookInstance)).thenReturn(bookInstanceDto);

        List<BookInstanceDto> result = bookInstanceServiceCRUD.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookInstanceDto.getId(), result.getFirst().getId());
        verify(bookInstanceRepository).findAll();
        verify(bookInstanceMapper).toDto(bookInstance);
    }

    @Test
    void findAll_ReturnsEmptyList() {
        when(bookInstanceRepository.findAll()).thenReturn(Collections.emptyList());

        List<BookInstanceDto> result = bookInstanceServiceCRUD.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(bookInstanceRepository).findAll();
        verify(bookInstanceMapper, never()).toDto(any());
    }

    @Test
    void update_ReturnsUpdatedBookInstanceDto() {
        when(bookInstanceMapper.toEntity(bookInstanceDto)).thenReturn(bookInstance);
        when(bookInstanceRepository.update(bookInstance)).thenReturn(Optional.of(bookInstance));
        when(bookInstanceMapper.toDto(bookInstance)).thenReturn(bookInstanceDto);

        BookInstanceDto result = bookInstanceServiceCRUD.update(bookInstanceDto);

        assertNotNull(result);
        assertEquals(bookInstanceDto.getId(), result.getId());
        verify(bookInstanceMapper).toEntity(bookInstanceDto);
        verify(bookInstanceRepository).update(bookInstance);
        verify(bookInstanceMapper).toDto(bookInstance);
    }

    @Test
    void update_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookInstanceServiceCRUD.update(null));
        verify(bookInstanceMapper, never()).toEntity(any());
        verify(bookInstanceRepository, never()).update(any());
    }

    @Test
    void update_ThrowsBookInstanceNotFoundException() {
        when(bookInstanceMapper.toEntity(bookInstanceDto)).thenReturn(bookInstance);
        when(bookInstanceRepository.update(bookInstance)).thenReturn(Optional.empty());

        assertThrows(BookInstanceNotFoundException.class, () -> bookInstanceServiceCRUD.update(bookInstanceDto));
        verify(bookInstanceMapper).toEntity(bookInstanceDto);
        verify(bookInstanceRepository).update(bookInstance);
        verify(bookInstanceMapper, never()).toDto(any());
    }

    @Test
    void delete_DeletesBookInstanceDto() {
        when(bookInstanceRepository.findById(1L)).thenReturn(Optional.of(bookInstance));

        bookInstanceServiceCRUD.delete(bookInstanceDto);

        verify(bookInstanceRepository).findById(1L);
        verify(bookInstanceRepository).deleteById(1L);
    }

    @Test
    void delete_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookInstanceServiceCRUD.delete(null));
        verify(bookInstanceRepository, never()).findById(anyLong());
        verify(bookInstanceRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_ThrowsBookInstanceNotFoundException() {
        when(bookInstanceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookInstanceNotFoundException.class, () -> bookInstanceServiceCRUD.delete(bookInstanceDto));
        verify(bookInstanceRepository).findById(1L);
        verify(bookInstanceRepository, never()).deleteById(anyLong());
    }
}