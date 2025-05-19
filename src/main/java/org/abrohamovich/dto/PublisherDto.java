package org.abrohamovich.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublisherDto {
    private long id;
    @NonNull
    private String name;
    @NonNull
    private LocalDate foundationDate;
    private String address;
    private String email;
    private String website;

    @Override
    public String toString() {
        return "PublisherDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", foundationDate=" + foundationDate +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
