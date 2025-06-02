package org.abrohamovich.mapper;

import org.abrohamovich.dto.BookDto;
import org.abrohamovich.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "patron", ignore = true)
    BookDto toDto(Book book);

    Book toEntity(BookDto bookDto);
}
