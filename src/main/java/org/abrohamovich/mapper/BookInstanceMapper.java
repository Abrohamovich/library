package org.abrohamovich.mapper;

import org.abrohamovich.dto.BookInstanceDto;
import org.abrohamovich.entity.BookInstance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookInstanceMapper {
    BookInstanceMapper INSTANCE = Mappers.getMapper(BookInstanceMapper.class);

    BookInstanceDto toDto(BookInstance bookInstance);

    BookInstance toEntity(BookInstanceDto bookInstanceDto);
}
