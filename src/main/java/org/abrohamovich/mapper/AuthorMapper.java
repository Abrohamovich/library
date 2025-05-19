package org.abrohamovich.mapper;

import org.abrohamovich.dto.AuthorDto;
import org.abrohamovich.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorDto toDto(Author author);

    Author toEntity(AuthorDto authorDto);
}
