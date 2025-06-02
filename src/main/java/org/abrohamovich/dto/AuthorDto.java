package org.abrohamovich.dto;

import lombok.*;
import org.abrohamovich.entity.Sex;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id", "fullName", "dateOfBirth", "nationality", "sex"})
public class AuthorDto {
    private long id;
    private String fullName;
    private LocalDate dateOfBirth;
    private Sex sex;
    private String nationality;

    @Override
    public String toString() {
        return fullName + ", " + nationality;
    }
}
