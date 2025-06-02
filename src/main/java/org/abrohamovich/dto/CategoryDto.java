package org.abrohamovich.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id", "name", "description"})
public class CategoryDto {
    private long id;
    private String name;
    private String description;

    @Override
    public String toString() {
        return name;
    }
}
