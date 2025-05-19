package org.abrohamovich.mapper;

import org.abrohamovich.dto.CategoryDto;
import org.abrohamovich.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "description", target = "description")
    CategoryDto toDto(Category category);

    @Mapping(source = "description", target = "description")
    Category toEntity(CategoryDto categoryDto);
}
