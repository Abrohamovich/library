package org.abrohamovich.mapper;

import org.abrohamovich.dto.PublisherDto;
import org.abrohamovich.entity.Publisher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PublisherMapper {
    PublisherMapper INSTANCE = Mappers.getMapper(PublisherMapper.class);

    PublisherDto toDto(Publisher publisher);

    Publisher toEntity(PublisherDto publisherDto);
}
