package org.abrohamovich.mapper;

import org.abrohamovich.dto.PatronDto;
import org.abrohamovich.entity.Patron;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PatronMapper {
    PatronMapper INSTANCE = Mappers.getMapper(PatronMapper.class);

    @Mapping(target = "books", ignore = true)
    PatronDto toDto(Patron patron);

    Patron toEntity(PatronDto patronDto);
}
