package org.abrohamovich.dto;

import lombok.*;
import org.abrohamovich.entity.Sex;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id", "fullName", "dateOfBirth", "nationality"})
public class AuthorDto {
    private long id;
    @NonNull
    private String fullName;
    @NonNull
    private LocalDate dateOfBirth;
    @NonNull
    private Sex sex;
    @NonNull
    private String nationality;

    @Override
    public String toString() {
        return fullName + ", " + nationality;
    }
}
