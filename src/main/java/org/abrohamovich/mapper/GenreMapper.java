package org.abrohamovich.mapper;

import org.abrohamovich.dto.GenreDto;
import org.abrohamovich.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GenreMapper {
    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    GenreDto toDto(Genre genre);

    Genre toEntity(GenreDto genreDto);
}
