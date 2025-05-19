package org.abrohamovich.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@Entity
@Table(name = "t_author")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "fullName", "dateOfBirth", "sex", "nationality"})
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
    @Column(name = "sex", nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Column(name = "nationality", nullable = false)
    private String nationality;
}
