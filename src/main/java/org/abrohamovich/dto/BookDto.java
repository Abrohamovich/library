package org.abrohamovich.dto;

import lombok.*;
import org.abrohamovich.entity.Format;
import org.abrohamovich.entity.Status;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
    private long id;
    private String title;
    private String isbn;
    private String language;
    private int numberOfPages;
    private PatronDto patron;
    @Builder.Default
    private Set<AuthorDto> authors = new HashSet<>();
    @Builder.Default
    private Set<GenreDto> genres = new HashSet<>();
    @Builder.Default
    private Set<CategoryDto> categories = new HashSet<>();
    private PublisherDto publisher;
    private Status status;
    private Format format;
    private LocalDate receiptDate;

    @Override
    public String toString() {
        return title;
    }
}
