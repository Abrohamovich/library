package org.abrohamovich.service;

import org.abrohamovich.dto.BookDto;
import org.abrohamovich.dto.BookInstanceDto;
import org.abrohamovich.dto.PatronDto;
import org.abrohamovich.dto.PublisherDto;
import org.abrohamovich.entity.Format;
import org.abrohamovich.entity.Patron;
import org.abrohamovich.entity.Status;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.exceptions.PatronAlreadyExistException;
import org.abrohamovich.exceptions.PatronNotFoundException;
import org.abrohamovich.mapper.PatronMapper;
import org.abrohamovich.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatronServiceCRUDTest {
    @InjectMocks
    private PatronServiceCRUD patronServiceCRUD;
    @Mock
    private PatronRepository patronRepository;
    @Mock
    private PatronMapper patronMapper;
    private Patron patron;
    private PatronDto patronDto;
    private BookInstanceDto bookInstanceDto;

    @BeforeEach
    void setUp() {
        patron = Patron.builder()
                .id(1L).email("test@gmail.com").phone("123456789")
                .cardId("WG$EE$y4574573").fullName("Name")
                .address("address").books(Set.of())
                .dateOfBirth(LocalDate.now()).registerDate(LocalDate.now())
                .build();
        patronDto = PatronDto.builder()
                .id(1L).email("test@gmail.com").phone("123456789")
                .cardId("WG$EE$y4574573").fullName("Name")
                .address("address").books(Set.of())
                .dateOfBirth(LocalDate.now()).registerDate(LocalDate.now())
                .build();
        bookInstanceDto = BookInstanceDto.builder().id(1L)
                .book(mock(BookDto.class))
                .publisher(mock(PublisherDto.class))
                .numberOfPages(130)
                .receiptDate(LocalDate.now())
                .status(Status.AVAILABLE)
                .format(Format.PAPERBACK)
                .build();
    }

    @Test
    void save_ReturnsPatronDto() {
        when(patronRepository.findByCardId(patronDto.getCardId())).thenReturn(Optional.empty());
        when(patronMapper.toEntity(patronDto)).thenReturn(patron);
        when(patronRepository.save(patron)).thenReturn(Optional.of(patron));
        when(patronMapper.toDto(patron)).thenReturn(patronDto);

        PatronDto result = patronServiceCRUD.save(patronDto);

        assertNotNull(result);
        assertEquals(patronDto, result);
        verify(patronRepository).save(patron);
        verify(patronMapper).toDto(patron);
    }

    @Test
    void save_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> patronServiceCRUD.save(null));
        verify(patronRepository, never()).save(any());
    }

    @Test
    void save_ThrowsPatronAlreadyExistException() {
        when(patronRepository.findByCardId(patronDto.getCardId())).thenReturn(Optional.of(patron));

        assertThrows(PatronAlreadyExistException.class,
                () -> patronServiceCRUD.save(patronDto));
        verify(patronRepository, never()).save(any());
    }

    @Test
    void save_ThrowsEntityException() {
        when(patronRepository.findByCardId(patronDto.getCardId())).thenReturn(Optional.empty());
        when(patronMapper.toEntity(patronDto)).thenReturn(patron);
        when(patronRepository.save(patron)).thenReturn(Optional.empty());

        assertThrows(EntityException.class,
                () -> patronServiceCRUD.save(patronDto));
        verify(patronRepository).save(patron);
    }

    @Test
    void findById_ReturnsPatronDto() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(patronMapper.toDto(patron)).thenReturn(patronDto);

        PatronDto result = patronServiceCRUD.findById(1L);

        assertNotNull(result);
        assertEquals(patronDto, result);
        verify(patronRepository).findById(1L);
        verify(patronMapper).toDto(patron);
    }

    @Test
    void findById_ThrowsPatronNotFoundException() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatronNotFoundException.class,
                () -> patronServiceCRUD.findById(1L));
        verify(patronRepository).findById(1L);
        verify(patronMapper, never()).toDto(any());
    }

    @Test
    void findByCardId_ReturnsPatronDto() {
        when(patronRepository.findByCardId("WG$EE$y4574573")).thenReturn(Optional.of(patron));
        when(patronMapper.toDto(patron)).thenReturn(patronDto);

        PatronDto result = patronServiceCRUD.findByCardId("WG$EE$y4574573");

        assertNotNull(result);
        assertEquals(patronDto, result);
        verify(patronRepository).findByCardId("WG$EE$y4574573");
        verify(patronMapper).toDto(patron);
    }

    @Test
    void findByCardId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> patronServiceCRUD.findByCardId(""));
        verify(patronRepository, never()).findByCardId(anyString());
    }

    @Test
    void findByCardId_ThrowsPatronNotFoundException() {
        when(patronRepository.findByCardId("WG$EE$y4574573")).thenReturn(Optional.empty());

        assertThrows(PatronNotFoundException.class,
                () -> patronServiceCRUD.findByCardId("WG$EE$y4574573"));
        verify(patronRepository).findByCardId("WG$EE$y4574573");
    }

    @Test
    void findByBookInstance_ReturnsPatronDto() {
        when(patronRepository.findByBookInstanceId(1L)).thenReturn(Optional.of(patron));
        when(patronMapper.toDto(patron)).thenReturn(patronDto);

        PatronDto result = patronServiceCRUD.findByBookInstance(bookInstanceDto);

        assertNotNull(result);
        assertEquals(patronDto, result);
        verify(patronRepository).findByBookInstanceId(1L);
        verify(patronMapper).toDto(patron);
    }

    @Test
    void findByBookInstance_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> patronServiceCRUD.findByBookInstance(null));
        verify(patronRepository, never()).findByBookInstanceId(anyLong());
    }

    @Test
    void findByBookInstance_ThrowsPatronNotFoundException() {
        when(patronRepository.findByBookInstanceId(1L)).thenReturn(Optional.empty());

        assertThrows(PatronNotFoundException.class,
                () -> patronServiceCRUD.findByBookInstance(bookInstanceDto));
        verify(patronRepository).findByBookInstanceId(1L);
    }

    @Test
    void findByFullName_ReturnsPatronDtoList() {
        List<Patron> patrons = List.of(patron);
        when(patronRepository.findByFullName("Name")).thenReturn(patrons);
        when(patronMapper.toDto(patron)).thenReturn(patronDto);

        List<PatronDto> result = patronServiceCRUD.findByFullName("Name");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(patronDto, result.getFirst());
        verify(patronRepository).findByFullName("Name");
        verify(patronMapper).toDto(patron);
    }

    @Test
    void findByFullName_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> patronServiceCRUD.findByFullName(""));
        verify(patronRepository, never()).findByFullName(anyString());
    }

    @Test
    void findByEmail_ReturnsPatronDto() {
        when(patronRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(patron));
        when(patronMapper.toDto(patron)).thenReturn(patronDto);

        PatronDto result = patronServiceCRUD.findByEmail("test@gmail.com");

        assertNotNull(result);
        assertEquals(patronDto, result);
        verify(patronRepository).findByEmail("test@gmail.com");
        verify(patronMapper).toDto(patron);
    }

    @Test
    void findByEmail_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> patronServiceCRUD.findByEmail(""));
        verify(patronRepository, never()).findByEmail(anyString());
    }

    @Test
    void findByEmail_ThrowsPatronNotFoundException() {
        when(patronRepository.findByEmail("test@gmail.com")).thenReturn(Optional.empty());

        assertThrows(PatronNotFoundException.class,
                () -> patronServiceCRUD.findByEmail("test@gmail.com"));
        verify(patronRepository).findByEmail("test@gmail.com");
    }

    @Test
    void findByPhone_ReturnsPatronDto() {
        when(patronRepository.findByPhone("123456789")).thenReturn(Optional.of(patron));
        when(patronMapper.toDto(patron)).thenReturn(patronDto);

        PatronDto result = patronServiceCRUD.findByPhone("123456789");

        assertNotNull(result);
        assertEquals(patronDto, result);
        verify(patronRepository).findByPhone("123456789");
        verify(patronMapper).toDto(patron);
    }

    @Test
    void findByPhone_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> patronServiceCRUD.findByPhone(""));
        verify(patronRepository, never()).findByPhone(anyString());
    }

    @Test
    void findByPhone_ThrowsPatronNotFoundException() {
        when(patronRepository.findByPhone("123456789")).thenReturn(Optional.empty());

        assertThrows(PatronNotFoundException.class,
                () -> patronServiceCRUD.findByPhone("123456789"));
        verify(patronRepository).findByPhone("123456789");
    }

    @Test
    void findByAddress_ReturnsPatronDto() {
        when(patronRepository.findByAddress("address")).thenReturn(Optional.of(patron));
        when(patronMapper.toDto(patron)).thenReturn(patronDto);

        PatronDto result = patronServiceCRUD.findByAddress("address");

        assertNotNull(result);
        assertEquals(patronDto, result);
        verify(patronRepository).findByAddress("address");
        verify(patronMapper).toDto(patron);
    }

    @Test
    void findByAddress_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> patronServiceCRUD.findByAddress(""));
        verify(patronRepository, never()).findByAddress(anyString());
    }

    @Test
    void findByAddress_ThrowsPatronNotFoundException() {
        when(patronRepository.findByAddress("address")).thenReturn(Optional.empty());

        assertThrows(PatronNotFoundException.class,
                () -> patronServiceCRUD.findByAddress("address"));
        verify(patronRepository).findByAddress("address");
    }

    @Test
    void findAll_ReturnsPatronDtoList() {
        List<Patron> patrons = List.of(patron);
        when(patronRepository.findAll()).thenReturn(patrons);
        when(patronMapper.toDto(patron)).thenReturn(patronDto);

        List<PatronDto> result = patronServiceCRUD.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(patronDto, result.getFirst());
        verify(patronRepository).findAll();
        verify(patronMapper).toDto(patron);
    }

    @Test
    void findAll_ReturnsEmptyList() {
        when(patronRepository.findAll()).thenReturn(List.of());

        List<PatronDto> result = patronServiceCRUD.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(patronRepository).findAll();
        verify(patronMapper, never()).toDto(any());
    }

    @Test
    void update_ReturnsUpdatedPatronDto() {
        when(patronMapper.toEntity(patronDto)).thenReturn(patron);
        when(patronRepository.update(patron)).thenReturn(Optional.of(patron));
        when(patronMapper.toDto(patron)).thenReturn(patronDto);

        PatronDto result = patronServiceCRUD.update(patronDto);

        assertNotNull(result);
        assertEquals(patronDto, result);
        verify(patronRepository).update(patron);
        verify(patronMapper).toDto(patron);
    }

    @Test
    void update_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> patronServiceCRUD.update(null));
        verify(patronRepository, never()).update(any());
    }

    @Test
    void update_ThrowsPatronNotFoundException() {
        when(patronMapper.toEntity(patronDto)).thenReturn(patron);
        when(patronRepository.update(patron)).thenReturn(Optional.empty());

        assertThrows(PatronNotFoundException.class,
                () -> patronServiceCRUD.update(patronDto));
        verify(patronRepository).update(patron);
    }

    @Test
    void delete_DeletesPatron() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));

        patronServiceCRUD.delete(patronDto);

        verify(patronRepository).deleteById(1L);
    }

    @Test
    void delete_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> patronServiceCRUD.delete(null));
        verify(patronRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_ThrowsPatronNotFoundException() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatronNotFoundException.class,
                () -> patronServiceCRUD.delete(patronDto));
        verify(patronRepository, never()).deleteById(anyLong());
    }
}