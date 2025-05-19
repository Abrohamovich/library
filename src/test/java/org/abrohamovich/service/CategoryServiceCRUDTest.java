package org.abrohamovich.service;

import org.abrohamovich.dto.CategoryDto;
import org.abrohamovich.entity.Category;
import org.abrohamovich.exceptions.CategoryAlreadyExistException;
import org.abrohamovich.exceptions.CategoryNotFoundException;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.mapper.CategoryMapper;
import org.abrohamovich.repository.CategoryRepository;
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
public class CategoryServiceCRUDTest {
    @InjectMocks
    private CategoryServiceCRUD categoryServiceCRUD;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    private Category category1;
    private Category category2;
    private CategoryDto categoryDto1;
    private CategoryDto categoryDto2;

    @BeforeEach
    void setUp() {
        category1 = Category.builder().id(1L).name("Fiction").description("Narrative").build();
        category2 = Category.builder().id(2L).name("Science").description("Science books").build();
        categoryDto1 = CategoryDto.builder().id(1L).name("Fiction").description("Narrative").build();
        categoryDto2 = CategoryDto.builder().id(2L).name("Science").description("Science books").build();
    }

    @Test
    void save_ReturnsCategoryDto() {
        when(categoryRepository.findByName(categoryDto1.getName())).thenReturn(Optional.empty());
        when(categoryMapper.toEntity(categoryDto1)).thenReturn(category1);
        when(categoryRepository.save(category1)).thenReturn(Optional.of(category1));
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);

        CategoryDto result = categoryServiceCRUD.save(categoryDto1);

        assertNotNull(result);
        assertEquals(categoryDto1, result);
        verify(categoryRepository).findByName(categoryDto1.getName());
        verify(categoryMapper).toEntity(categoryDto1);
        verify(categoryRepository).save(category1);
        verify(categoryMapper).toDto(category1);
    }

    @Test
    void save_ThrowsCategoryAlreadyExistException() {
        when(categoryRepository.findByName(categoryDto1.getName())).thenReturn(Optional.of(category1));

        assertThrows(CategoryAlreadyExistException.class, () -> categoryServiceCRUD.save(categoryDto1));
        verify(categoryRepository).findByName(categoryDto1.getName());
        verify(categoryMapper, never()).toEntity(any());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void save_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> categoryServiceCRUD.save(null));
        verify(categoryRepository, never()).findByName(anyString());
        verify(categoryMapper, never()).toEntity(any());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void save_ThrowsEntityException() {
        when(categoryRepository.findByName(categoryDto1.getName())).thenReturn(Optional.empty());
        when(categoryMapper.toEntity(categoryDto1)).thenReturn(category1);
        when(categoryRepository.save(category1)).thenReturn(Optional.empty());

        assertThrows(EntityException.class, () -> categoryServiceCRUD.save(categoryDto1));
        verify(categoryRepository).findByName(categoryDto1.getName());
        verify(categoryMapper).toEntity(categoryDto1);
        verify(categoryRepository).save(category1);
        verify(categoryMapper, never()).toDto(any());
    }

    @Test
    void findById_ReturnsCategoryDto() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);

        CategoryDto result = categoryServiceCRUD.findById(1L);

        assertNotNull(result);
        assertEquals(categoryDto1, result);
        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toDto(category1);
    }

    @Test
    void findById_ThrowsCategoryNotFoundException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryServiceCRUD.findById(1L));
        verify(categoryRepository).findById(1L);
        verify(categoryMapper, never()).toDto(any());
    }

    @Test
    void findByName_ReturnsCategoryDto() {
        when(categoryRepository.findByName("Fiction")).thenReturn(Optional.of(category1));
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);

        CategoryDto result = categoryServiceCRUD.findByName("Fiction");

        assertNotNull(result);
        assertEquals(categoryDto1, result);
        verify(categoryRepository).findByName("Fiction");
        verify(categoryMapper).toDto(category1);
    }

    @Test
    void findByName_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> categoryServiceCRUD.findByName("  "));
        verify(categoryRepository, never()).findByName(anyString());
    }

    @Test
    void findByName_ThrowsCategoryNotFoundException() {
        when(categoryRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryServiceCRUD.findByName("Unknown"));
        verify(categoryRepository).findByName("Unknown");
        verify(categoryMapper, never()).toDto(any());
    }

    @Test
    void findAll_ReturnsCategoryDtoList() {
        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        when(categoryMapper.toDto(category2)).thenReturn(categoryDto2);

        List<CategoryDto> result = categoryServiceCRUD.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(categoryDto1));
        assertTrue(result.contains(categoryDto2));
        verify(categoryRepository).findAll();
        verify(categoryMapper).toDto(category1);
        verify(categoryMapper).toDto(category2);
    }

    @Test
    void update_ReturnsUpdatedCategoryDto() {
        when(categoryMapper.toEntity(categoryDto1)).thenReturn(category1);
        when(categoryRepository.update(category1)).thenReturn(Optional.of(category1));
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);

        CategoryDto result = categoryServiceCRUD.update(categoryDto1);

        assertNotNull(result);
        assertEquals(categoryDto1, result);
        verify(categoryMapper).toEntity(categoryDto1);
        verify(categoryRepository).update(category1);
        verify(categoryMapper).toDto(category1);
    }

    @Test
    void update_ThrowsCategoryNotFoundException() {
        when(categoryMapper.toEntity(categoryDto1)).thenReturn(category1);
        when(categoryRepository.update(category1)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryServiceCRUD.update(categoryDto1));
        verify(categoryMapper).toEntity(categoryDto1);
        verify(categoryRepository).update(category1);
        verify(categoryMapper, never()).toDto(any());
    }

    @Test
    void update_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> categoryServiceCRUD.update(null));
        verify(categoryMapper, never()).toEntity(any());
        verify(categoryRepository, never()).update(any());
    }

    @Test
    void delete_DeletesCategory() {
        when(categoryRepository.findById(categoryDto1.getId())).thenReturn(Optional.of(category1));

        categoryServiceCRUD.delete(categoryDto1);

        verify(categoryRepository).findById(categoryDto1.getId());
        verify(categoryRepository).deleteById(categoryDto1.getId());
    }

    @Test
    void delete_ThrowsCategoryNotFoundException() {
        when(categoryRepository.findById(categoryDto1.getId())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryServiceCRUD.delete(categoryDto1));
        verify(categoryRepository).findById(categoryDto1.getId());
        verify(categoryRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> categoryServiceCRUD.delete(null));
        verify(categoryRepository, never()).findById(anyLong());
        verify(categoryRepository, never()).deleteById(anyLong());
    }
}