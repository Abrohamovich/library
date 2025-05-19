package org.abrohamovich.service;

import org.abrohamovich.dto.PublisherDto;
import org.abrohamovich.entity.Publisher;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.exceptions.PublisherAlreadyExistException;
import org.abrohamovich.exceptions.PublisherNotFoundException;
import org.abrohamovich.mapper.PublisherMapper;
import org.abrohamovich.repository.PublisherRepository;
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
class PublisherServiceCRUDTest {
    @InjectMocks
    private PublisherServiceCRUD publisherServiceCRUD;
    @Mock
    private PublisherRepository repository;
    @Mock
    private PublisherMapper mapper;
    private Publisher publisher;
    private PublisherDto publisherDto;

    @BeforeEach
    void setUp() {
        publisher = Publisher.builder()
                .id(1L)
                .name("Penguin Random House")
                .foundationDate(LocalDate.of(2013, 7, 1))
                .email("info@penguinrandomhouse.com")
                .website("https://www.penguinrandomhouse.com")
                .address("1745 Broadway, New York, NY 10019, USA")
                .build();

        publisherDto = PublisherDto.builder()
                .id(1L)
                .name("Penguin Random House")
                .foundationDate(LocalDate.of(2013, 7, 1))
                .email("info@penguinrandomhouse.com")
                .website("https://www.penguinrandomhouse.com")
                .address("1745 Broadway, New York, NY 10019, USA")
                .build();
    }

    @Test
    void save_ReturnsPublisherDto() {
        when(repository.findByName(publisherDto.getName())).thenReturn(Optional.empty());
        when(mapper.toEntity(publisherDto)).thenReturn(publisher);
        when(repository.save(publisher)).thenReturn(Optional.of(publisher));
        when(mapper.toDto(publisher)).thenReturn(publisherDto);

        PublisherDto result = publisherServiceCRUD.save(publisherDto);

        assertNotNull(result);
        assertEquals(publisherDto, result);
        verify(repository).findByName(publisherDto.getName());
        verify(mapper).toEntity(publisherDto);
        verify(repository).save(publisher);
        verify(mapper).toDto(publisher);
    }

    @Test
    void save_ThrowsPublisherAlreadyExistException() {
        when(repository.findByName(publisherDto.getName())).thenReturn(Optional.of(publisher));

        assertThrows(PublisherAlreadyExistException.class, () -> publisherServiceCRUD.save(publisherDto));
        verify(repository).findByName(publisherDto.getName());
        verify(mapper, never()).toEntity(any());
        verify(repository, never()).save(any());
    }

    @Test
    void save_ThrowsEntityException() {
        when(repository.findByName(publisherDto.getName())).thenReturn(Optional.empty());
        when(mapper.toEntity(publisherDto)).thenReturn(publisher);
        when(repository.save(publisher)).thenReturn(Optional.empty());

        assertThrows(EntityException.class, () -> publisherServiceCRUD.save(publisherDto));
        verify(repository).findByName(publisherDto.getName());
        verify(mapper).toEntity(publisherDto);
        verify(repository).save(publisher);
        verify(mapper, never()).toDto(any());
    }

    @Test
    void save_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> publisherServiceCRUD.save(null));
        verify(repository, never()).findByName(anyString());
        verify(mapper, never()).toEntity(any());
        verify(repository, never()).save(any());
    }

    @Test
    void findById_ReturnsPublisherDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(publisher));
        when(mapper.toDto(publisher)).thenReturn(publisherDto);

        PublisherDto result = publisherServiceCRUD.findById(1L);

        assertNotNull(result);
        assertEquals(publisherDto, result);
        verify(repository).findById(1L);
        verify(mapper).toDto(publisher);
    }

    @Test
    void findById_ThrowsPublisherNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> publisherServiceCRUD.findById(1L));
        verify(repository).findById(1L);
        verify(mapper, never()).toDto(any());
    }

    @Test
    void findByName_ReturnsPublisherDto() {
        when(repository.findByName("Penguin Random House")).thenReturn(Optional.of(publisher));
        when(mapper.toDto(publisher)).thenReturn(publisherDto);

        PublisherDto result = publisherServiceCRUD.findByName("Penguin Random House");

        assertNotNull(result);
        assertEquals(publisherDto, result);
        verify(repository).findByName("Penguin Random House");
        verify(mapper).toDto(publisher);
    }

    @Test
    void findByName_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> publisherServiceCRUD.findByName(" "));
        verify(repository, never()).findByName(anyString());
    }

    @Test
    void findByName_ThrowsPublisherNotFoundException() {
        when(repository.findByName(any(String.class))).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> publisherServiceCRUD.findByName("name"));
        verify(repository).findByName(anyString());
        verify(mapper, never()).toDto(any());
    }

    @Test
    void findByEmail_ReturnsPublisherDto() {
        when(repository.findByEmail("info@penguinrandomhouse.com")).thenReturn(Optional.of(publisher));
        when(mapper.toDto(publisher)).thenReturn(publisherDto);

        PublisherDto result = publisherServiceCRUD.findByEmail("info@penguinrandomhouse.com");

        assertNotNull(result);
        assertEquals(publisherDto, result);
        verify(repository).findByEmail("info@penguinrandomhouse.com");
        verify(mapper).toDto(publisher);
    }

    @Test
    void findByEmail_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> publisherServiceCRUD.findByEmail(" "));
        verify(repository, never()).findByEmail(anyString());
    }

    @Test
    void findByEmail_ThrowsPublisherNotFoundException() {
        when(repository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> publisherServiceCRUD.findByEmail("name"));
        verify(repository).findByEmail(anyString());
        verify(mapper, never()).toDto(any());
    }

    @Test
    void findByWebsite_ReturnsPublisherDto() {
        when(repository.findByWebsite(publisherDto.getWebsite())).thenReturn(Optional.of(publisher));
        when(mapper.toDto(publisher)).thenReturn(publisherDto);

        PublisherDto result = publisherServiceCRUD.findByWebsite(publisherDto.getWebsite());

        assertNotNull(result);
        assertEquals(publisherDto, result);
        verify(repository).findByWebsite(publisherDto.getWebsite());
        verify(mapper).toDto(publisher);
    }

    @Test
    void findByWebsite_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> publisherServiceCRUD.findByWebsite(" "));
        verify(repository, never()).findByWebsite(anyString());
    }

    @Test
    void findByWebsite_ThrowsPublisherNotFoundException() {
        when(repository.findByWebsite(any(String.class))).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> publisherServiceCRUD.findByWebsite("name"));
        verify(repository).findByWebsite(anyString());
        verify(mapper, never()).toDto(any());
    }

    @Test
    void findByAddress_ReturnsPublisherDto() {
        when(repository.findByAddress(publisherDto.getAddress())).thenReturn(Optional.of(publisher));
        when(mapper.toDto(publisher)).thenReturn(publisherDto);

        PublisherDto result = publisherServiceCRUD.findByAddress(publisherDto.getAddress());

        assertNotNull(result);
        assertEquals(publisherDto, result);
        verify(repository).findByAddress(publisherDto.getAddress());
        verify(mapper).toDto(publisher);
    }

    @Test
    void findByAddress_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> publisherServiceCRUD.findByAddress(" "));
        verify(repository, never()).findByAddress(anyString());
    }

    @Test
    void findByAddress_ThrowsPublisherNotFoundException() {
        when(repository.findByAddress(any(String.class))).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> publisherServiceCRUD.findByAddress("name"));
        verify(repository).findByAddress(anyString());
        verify(mapper, never()).toDto(any());
    }

    @Test
    void findAll_ReturnsPublisherDtoList() {
        when(repository.findAll()).thenReturn(List.of(publisher));
        when(mapper.toDto(publisher)).thenReturn(publisherDto);

        List<PublisherDto> result = publisherServiceCRUD.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(publisherDto, result.getFirst());
        verify(repository).findAll();
        verify(mapper).toDto(publisher);
    }

    @Test
    void update_ReturnsUpdatedPublisherDto() {
        when(mapper.toEntity(publisherDto)).thenReturn(publisher);
        when(repository.update(publisher)).thenReturn(Optional.of(publisher));
        when(mapper.toDto(publisher)).thenReturn(publisherDto);

        PublisherDto result = publisherServiceCRUD.update(publisherDto);

        assertNotNull(result);
        assertEquals(publisherDto, result);
        verify(mapper).toEntity(publisherDto);
        verify(repository).update(publisher);
        verify(mapper).toDto(publisher);
    }

    @Test
    void update_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> publisherServiceCRUD.update(null));
        verify(mapper, never()).toEntity(any());
        verify(repository, never()).update(any());
    }

    @Test
    void update_ThrowsPublisherNotFoundException() {
        when(mapper.toEntity(publisherDto)).thenReturn(publisher);
        when(repository.update(publisher)).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> publisherServiceCRUD.update(publisherDto));
        verify(mapper).toEntity(publisherDto);
        verify(repository).update(publisher);
        verify(mapper, never()).toDto(any());
    }

    @Test
    void delete_DeletesPublisher() {
        when(repository.findById(publisherDto.getId())).thenReturn(Optional.of(publisher));

        publisherServiceCRUD.delete(publisherDto);

        verify(repository).findById(publisherDto.getId());
        verify(repository).deleteById(publisherDto.getId());
    }

    @Test
    void delete_ThrowsPublisherNotFoundException() {
        when(repository.findById(publisherDto.getId())).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> publisherServiceCRUD.delete(publisherDto));
        verify(repository).findById(publisherDto.getId());
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void delete_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> publisherServiceCRUD.delete(null));
        verify(repository, never()).findById(anyLong());
        verify(repository, never()).deleteById(anyLong());
    }
}