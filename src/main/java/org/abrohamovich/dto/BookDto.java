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
    @NonNull
    private String title;
    @NonNull
    private String isbn;
    @NonNull
    private String language;
    private int numberOfPages;
    @Builder.Default
    private Set<AuthorDto> authors = new HashSet<>();
    @Builder.Default
    private Set<GenreDto> genres = new HashSet<>();
    @Builder.Default
    private Set<CategoryDto> categories = new HashSet<>();
    @NonNull
    private PublisherDto publisher;
    @NonNull
    private Status status;
    @NonNull
    private Format format;
    @NonNull
    private LocalDate receiptDate;

    @Override
    public String toString() {
        String authorsStr = authors.stream()
                .map(AuthorDto::toString)
                .collect(Collectors.joining("; "));
        return title + " - " + authorsStr;
    }
}
