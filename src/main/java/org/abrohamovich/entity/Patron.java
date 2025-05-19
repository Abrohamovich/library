package org.abrohamovich.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Entity
@Table(name = "t_patron")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"books"})
public class Patron {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "card_id", nullable = false, unique = true)
    private String cardId;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "t_patron_book_instance",
            joinColumns = @JoinColumn(name = "patron_id"),
            inverseJoinColumns = @JoinColumn(name = "book_instance_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = "book_instance_id")
    )
    @Builder.Default
    private Set<BookInstance> books = new HashSet<>();
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;
    @Column(name = "address", nullable = false, unique = true)
    private String address;
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
    @Column(name = "register_date", nullable = false)
    private LocalDate registerDate;

    public boolean addBookInstance(BookInstance bookInstance) {
        return books.add(bookInstance);
    }

    public boolean removeBookInstance(BookInstance bookInstance) {
        return books.remove(bookInstance);
    }
}
