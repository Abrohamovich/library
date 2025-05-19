package org.abrohamovich.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
    private long id;
    @NonNull
    private String title;
    @Builder.Default
    private Set<AuthorDto> authors = new HashSet<>();
    @NonNull
    private String isbn;
    @Builder.Default
    private Set<GenreDto> genres = new HashSet<>();
    @NonNull
    private String language;
    @Builder.Default
    private Set<CategoryDto> categories = new HashSet<>();

    @Override
    public String toString() {
        return "BookDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authors=" + authors +
                ", isbn='" + isbn + '\'' +
                ", genres=" + genres +
                ", language='" + language + '\'' +
                ", categories=" + categories +
                '}';
    }
}
