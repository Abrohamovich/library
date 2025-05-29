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
public class PatronDto {
    private long id;
    @NonNull
    private String cardId;
    @NonNull
    private String fullName;
    @Builder.Default
    private Set<BookDto> books = new HashSet<>();
    @NonNull
    private String email;
    @NonNull
    private String phone;
    private String address;
    @NonNull
    private LocalDate dateOfBirth;
    @NonNull
    private LocalDate registerDate;

    @Override
    public String toString() {
        return fullName + ", '" + cardId + "'";
    }
}
