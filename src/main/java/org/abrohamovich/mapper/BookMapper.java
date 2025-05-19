package org.abrohamovich.mapper;

import org.abrohamovich.dto.BookDto;
import org.abrohamovich.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDto toDto(Book book);

    Book toEntity(BookDto bookDto);
}
