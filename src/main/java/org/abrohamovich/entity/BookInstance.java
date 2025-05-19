package org.abrohamovich.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@Entity
@Table(name = "t_book_instance")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "numberOfPages", "receiptDate", "status", "format"})
public class BookInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @Column(name = "number_of_pages", nullable = false)
    private int numberOfPages;

    @Column(name = "receipt_date", nullable = false)
    private LocalDate receiptDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Format format;
}
