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

class BookRepositoryTest {
    private BookRepository repo;
    private EntityManagerFactory emf;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("test-persistence-unit");
        repo = new BookRepository(emf);

        Author author1 = Author.builder()
                .fullName("Jane Austen").dateOfBirth(LocalDate.of(1775, 12, 16)).sex(Sex.FEMALE).nationality("British")
                .build();

        Author author2 = Author.builder()
                .fullName("Gabriel García Márquez").dateOfBirth(LocalDate.of(1927, 3, 6)).sex(Sex.MALE).nationality("Colombian")
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

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(author1);
            em.persist(author2);
            em.persist(genre1);
            em.persist(genre2);
            em.persist(genre3);
            em.persist(category1);
            em.persist(category2);
            em.persist(category3);
            em.persist(publisher1);
            em.persist(publisher2);
            em.getTransaction().commit();
        }

        book1 = Book.builder().title("Some title 1").isbn("978-9-4146-4771-1").language("english").numberOfPages(130)
                .authors(Set.of(author1)).genres(Set.of(genre1)).categories(Set.of(category1, category2))
                .publisher(publisher1).status(Status.AVAILABLE).format(Format.PAPERBACK).receiptDate(LocalDate.now())
                .build();
        book2 = Book.builder().title("SSome title 2").isbn("978-0-9937-6855-2").language("russian").numberOfPages(130)
                .authors(Set.of(author2)).genres(Set.of(genre1, genre2)).categories(Set.of(category3))
                .publisher(publisher1).status(Status.CHECKED_OUT).format(Format.HARDCOVER).receiptDate(LocalDate.now())
                .build();

        book3 = Book.builder().title("Some title 3").isbn("978-0-7247-2925-8").language("ukrainian").numberOfPages(130)
                .authors(Set.of(author1, author2)).genres(Set.of(genre3)).categories(Set.of(category2))
                .publisher(publisher2).status(Status.NOT_AVAILABLE).format(Format.PAPERBACK).receiptDate(LocalDate.now())
                .build();

    }

    @AfterEach
    void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    void persistBooks() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(book1);
            em.persist(book2);
            em.persist(book3);
            em.getTransaction().commit();
        }
    }

    @Test
    void save_ReturnsOptionalOfBook() {
        Optional<Book> saved1 = repo.save(book1);
        Optional<Book> saved2 = repo.save(book2);
        Optional<Book> saved3 = repo.save(book3);

        assertTrue(saved1.isPresent());
        assertTrue(saved2.isPresent());
        assertTrue(saved3.isPresent());

        assertEquals(book1, saved1.get());
        assertEquals(book2, saved2.get());
        assertEquals(book3, saved3.get());
    }

    @Test
    void findById_ReturnsOptionalOfBook() {
        persistBooks();

        Optional<Book> saved1 = repo.findById(book1.getId());
        Optional<Book> saved2 = repo.findById(book2.getId());
        Optional<Book> saved3 = repo.findById(book3.getId());

        assertTrue(saved1.isPresent());
        assertTrue(saved2.isPresent());
        assertTrue(saved3.isPresent());

        assertEquals(book1, saved1.get());
        assertEquals(book2, saved2.get());
        assertEquals(book3, saved3.get());
    }

    @Test
    void findByAuthorIds_ReturnsListOfBooks() {
        persistBooks();

        List<Book> res = repo.findByAuthorIds(List.of(1L, 2L));

        assertEquals(book3, res.getFirst());
    }

    @Test
    void findByIsbn_ReturnsListOfBooks() {
        persistBooks();

        List<Book> res = repo.findByIsbn("978-0-9937-6855-2");

        assertEquals(1, res.size());
        assertEquals(book2, res.getFirst());
    }

    @Test
    void findByGenreIds_ReturnsListOfBooks() {
        persistBooks();

        List<Book> res1 = repo.findByGenreIds(List.of(1L));
        List<Book> res2 = repo.findByGenreIds(List.of(1L, 2L));

        assertEquals(book1, res1.getFirst());
        assertEquals(book2, res2.getFirst());
    }

    @Test
    void findByCategoryIds_ReturnsListOfBooks() {
        persistBooks();

        List<Book> res = repo.findByCategoryIds(List.of(3L));

        assertEquals(book2, res.getFirst());
    }

    @Test
    void findByPublisherId_ReturnsBookInstances() {
        persistBooks();

        List<Book> res = repo.findByPublisherId(2L);

        assertEquals(book3, res.getFirst());
    }

    @Test
    void findByStatus_ReturnsBookInstances() {
        persistBooks();

        List<Book> res = repo.findByStatus(Status.AVAILABLE);

        assertEquals(book1, res.getFirst());
    }

    @Test
    void findByFormat_ReturnsBookInstances() {
        persistBooks();

        List<Book> res = repo.findByFormat(Format.HARDCOVER);

        assertEquals(book2, res.getFirst());
    }

    @Test
    void findByLanguage_ReturnsListOfBooks() {
        persistBooks();

        List<Book> res1 = repo.findByLanguage("english");

        assertEquals(book1, res1.getFirst());
    }

    @Test
    void findAll_ReturnsListOfAllBooks() {
        persistBooks();

        List<Book> res = repo.findAll();

        assertArrayEquals(List.of(book1, book2, book3).toArray(), res.toArray());
    }

    @Test
    void update_UpdatesExistingBook() {
        persistBooks();

        Genre g = Genre.builder().name("some genre").description("some description").build();

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(g);
            em.getTransaction().commit();
        }

        book1.setGenres(Set.of(g));

        repo.update(book1);

        try (EntityManager em = emf.createEntityManager()) {
            Book updatedBook = em.find(Book.class, book1.getId());
            assertNotNull(updatedBook);
            assertEquals(1, updatedBook.getGenres().size());
            assertEquals("some genre", updatedBook.getGenres().iterator().next().getName());
        }
    }

    @Test
    void deleteById_DeletesBook() {
        persistBooks();

        repo.deleteById(book1.getId());
        repo.deleteById(book2.getId());

        try (EntityManager em = emf.createEntityManager()) {
            assertNull(em.find(Book.class, book1.getId()));
            assertNull(em.find(Book.class, book2.getId()));
            assertNotNull(em.find(Book.class, book3.getId()));
        }
    }
}