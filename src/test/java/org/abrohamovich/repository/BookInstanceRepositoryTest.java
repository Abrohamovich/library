package org.abrohamovich.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.abrohamovich.entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookInstanceRepositoryTest {
    private BookInstanceRepository repo;
    private EntityManagerFactory emf;
    private BookInstance bi1;
    private BookInstance bi2;
    private BookInstance bi3;
    private BookInstance bi4;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("test-persistence-unit");
        repo = new BookInstanceRepository(emf);

        Author author1 = Author.builder()
                .fullName("Jane Austen").dateOfBirth(LocalDate.of(1775, 12, 16)).sex(Sex.FEMALE).nationality("British")
                .build();

        Author author2 = Author.builder()
                .fullName("Gabriel García Márquez").dateOfBirth(LocalDate.of(1927, 3, 6)).sex(Sex.MALE).nationality("Colombian")
                .build();

        Publisher publisher1 = Publisher.builder()
                .name("Penguin Random House")
                .foundationDate(LocalDate.of(1927, 7, 1))
                .address("1745 Broadway, New York, NY 10019, USA")
                .email("contact@penguinrandomhouse.com")
                .website("https://www.penguinrandomhouse.com")
                .build();
        Publisher publisher2 = Publisher.builder()
                .name("HarperCollins")
                .foundationDate(LocalDate.of(1989, 8, 1))
                .address("195 Broadway, New York, NY 10007, USA")
                .email("info@harpercollins.com")
                .website("https://www.harpercollins.com")
                .build();

        Genre genre1 = Genre.builder()
                .name("Thriller")
                .description("Genre characterized by suspense, excitement, and tension")
                .build();
        Genre genre2 = Genre.builder()
                .name("Romance")
                .description("Genre centered around romantic relationships between characters")
                .build();
        Genre genre3 = Genre.builder()
                .name("Horror")
                .description("Genre intended to scare, unsettle, or horrify the reader")
                .build();

        Category category1 = Category.builder()
                .name("Fiction")
                .description("Narrative literature created from the imagination, not presented as fact.")
                .build();

        Category category2 = Category.builder()
                .name("Science")
                .description("Books that explain scientific concepts and discoveries.")
                .build();

        Category category3 = Category.builder()
                .name("History")
                .description("Books that explore past events and historical figures.")
                .build();

        Book book1 = Book.builder()
                .title("Some title 1").authors(Set.of(author1)).isbn("978-9-4146-4771-1")
                .genres(Set.of(genre1)).language("english").categories(Set.of(category1, category2))
                .build();
        Book book2 = Book.builder()
                .title("Some title 2").authors(Set.of(author2)).isbn("978-0-9937-6855-2")
                .genres(Set.of(genre1, genre2)).language("german").categories(Set.of(category3))
                .build();
        Book book3 = Book.builder()
                .title("Some title 3").authors(Set.of(author1)).isbn("978-0-7247-2925-8")
                .genres(Set.of(genre3)).language("russian").categories(Set.of(category2))
                .build();

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(author1);
            em.persist(author2);
            em.persist(publisher1);
            em.persist(publisher2);
            em.persist(genre1);
            em.persist(genre2);
            em.persist(genre3);
            em.persist(category1);
            em.persist(category2);
            em.persist(category3);
            em.persist(book1);
            em.persist(book2);
            em.persist(book3);
            em.getTransaction().commit();
        }

        bi1 = BookInstance.builder()
                .book(book1).numberOfPages(120).receiptDate(LocalDate.of(2025, 1, 23))
                .status(Status.AVAILABLE).publisher(publisher1).format(Format.HARDCOVER).build();
        bi2 = BookInstance.builder()
                .book(book2).numberOfPages(120).receiptDate(LocalDate.of(2025, 4, 13))
                .status(Status.NOT_AVAILABLE).publisher(publisher2).format(Format.HARDCOVER).build();
        bi3 = BookInstance.builder()
                .book(book3).numberOfPages(134).receiptDate(LocalDate.of(2025, 2, 7))
                .status(Status.AVAILABLE).publisher(publisher1).format(Format.PAPERBACK).build();
        bi4 = BookInstance.builder()
                .book(book3).numberOfPages(176).receiptDate(LocalDate.of(2021, 10, 18))
                .status(Status.CHECKED_OUT).publisher(publisher2).format(Format.HARDCOVER).build();
    }

    @AfterEach
    void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    void persist() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(bi1);
            em.persist(bi2);
            em.persist(bi3);
            em.persist(bi4);
            em.getTransaction().commit();
        }
    }

    @Test
    void save_ReturnsSavedBookInstances() {
        Optional<BookInstance> saved1 = repo.save(bi1);
        Optional<BookInstance> saved2 = repo.save(bi2);
        Optional<BookInstance> saved3 = repo.save(bi3);
        Optional<BookInstance> saved4 = repo.save(bi4);

        assertTrue(saved1.isPresent());
        assertTrue(saved2.isPresent());
        assertTrue(saved3.isPresent());
        assertTrue(saved4.isPresent());

        assertEquals(bi1, saved1.get());
        assertEquals(bi2, saved2.get());
        assertEquals(bi3, saved3.get());
        assertEquals(bi4, saved4.get());
    }

    @Test
    void findById_ReturnsBookInstance() {
        persist();

        Optional<BookInstance> res1 = repo.findById(bi1.getId());
        Optional<BookInstance> res3 = repo.findById(bi3.getId());

        assertTrue(res1.isPresent());
        assertTrue(res3.isPresent());

        assertEquals(bi1, res1.get());
        assertEquals(bi3, res3.get());
    }

    @Test
    void findByBookId_ReturnsBookInstances() {
        persist();

        List<BookInstance> res = repo.findByBookId(3);

        assertEquals(2, res.size());
        assertArrayEquals(List.of(bi3, bi4).toArray(), res.toArray());
    }

    @Test
    void findByPublisherId_ReturnsBookInstances() {
        persist();

        List<BookInstance> res = repo.findByPublisherId(2L);

        assertEquals(2, res.size());

        assertArrayEquals(List.of(bi2, bi4).toArray(), res.toArray());
    }

    @Test
    void findByStatus_ReturnsBookInstances() {
        persist();

        List<BookInstance> res = repo.findByStatus(Status.AVAILABLE);

        assertEquals(2, res.size());
    }

    @Test
    void findByFormat_ReturnsBookInstances() {
        persist();

        List<BookInstance> res = repo.findByFormat(Format.HARDCOVER);

        assertEquals(3, res.size());
    }

    @Test
    void findAll_ReturnsAllBookInstances() {
        persist();

        List<BookInstance> res = repo.findAll();

        assertEquals(4, res.size());
    }

    @Test
    void update_UpdatesBookInstanceFormat() {
        persist();

        bi1.setFormat(Format.PAPERBACK);

        repo.update(bi1);

        try (EntityManager em = emf.createEntityManager()) {
            assertEquals(Format.PAPERBACK, em.find(BookInstance.class, bi1.getId()).getFormat());
        }
    }

    @Test
    void deleteById_RemovesBookInstances() {
        persist();

        repo.deleteById(bi1.getId());
        repo.deleteById(bi2.getId());

        try (EntityManager em = emf.createEntityManager()) {
            assertNull(em.find(BookInstance.class, bi1.getId()));
            assertNull(em.find(BookInstance.class, bi2.getId()));
        }
    }
}