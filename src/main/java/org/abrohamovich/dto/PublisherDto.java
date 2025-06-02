package org.abrohamovich.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id", "name", "address", "email", "website"})
public class PublisherDto {
    private long id;
    private String name;
    private LocalDate foundationDate;
    private String address;
    private String email;
    private String website;

    @Override
    public String toString() {
        return name;
    }
}
