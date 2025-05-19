package org.abrohamovich.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@Entity
@Table(name = "t_publisher")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "foundation_date", nullable = false)
    private LocalDate foundationDate;
    @Column(name = "address", unique = true)
    private String address;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "website", unique = true)
    private String website;
}
