package org.abrohamovich.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"books"})
public class PatronDto {
    private long id;
    private String cardId;
    private String fullName;
    @Builder.Default
    private Set<BookDto> books = new HashSet<>();
    private String email;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private LocalDate registerDate;

    @Override
    public String toString() {
        return fullName + ", '" + cardId + "'";
    }
}
