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

class PatronRepositoryTest {
    private PatronRepository repo;
    private EntityManagerFactory emf;
    private Patron patron1;
    private Patron patron2;
    private Patron patron3;

    @BeforeEach
    void setUp() {

        emf = Persistence.createEntityManagerFactory("test-persistence-unit");
        repo = new PatronRepository(emf);

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

        Book book1 = Book.builder().title("Some title 1").isbn("978-9-4146-4771-1").language("").numberOfPages(130)
                .authors(Set.of(author1)).genres(Set.of(genre1)).categories(Set.of(category1, category2))
                .publisher(publisher1).status(Status.AVAILABLE).format(Format.PAPERBACK).receiptDate(LocalDate.now())
                .build();
        Book book3 = Book.builder().title("Some title 3").isbn("978-0-7247-2925-8").language("").numberOfPages(130)
                .authors(Set.of(author1, author2)).genres(Set.of(genre3)).categories(Set.of(category2))
                .publisher(publisher2).status(Status.NOT_AVAILABLE).format(Format.PAPERBACK).receiptDate(LocalDate.now())
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
            em.persist(book3);
            em.getTransaction().commit();
        }

        patron1 = Patron.builder().cardId("C1001").fullName("Alice Smith")
                .email("alice.smith@example.com").phone("123-456-7890")
                .address("123 Main St, Springfield")
                .dateOfBirth(LocalDate.of(1990, 5, 1))
                .registerDate(LocalDate.of(2024, 1, 15))
                .build();

        patron2 = Patron.builder().cardId("C1002").fullName("Bob Johnson")
                .email("bob.johnson@example.com").phone("987-654-3210")
                .address("456 Oak Ave, Riverdale")
                .dateOfBirth(LocalDate.of(1985, 11, 20))
                .registerDate(LocalDate.of(2024, 3, 10))
                .build();

        patron3 = Patron.builder().cardId("C1003").fullName("Carol Williams")
                .email("carol.williams@example.com").phone("555-123-4567")
                .address("789 Pine Rd, Greenville")
                .dateOfBirth(LocalDate.of(1992, 7, 15))
                .registerDate(LocalDate.of(2024, 4, 5))
                .build();
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
            em.persist(patron1);
            em.persist(patron2);
            em.persist(patron3);
            em.getTransaction().commit();
        }
    }

    @Test
    void save_ReturnsOptionalOfSavedPatron() {
        Optional<Patron> saved1 = repo.save(patron1);
        Optional<Patron> saved2 = repo.save(patron2);
        Optional<Patron> saved3 = repo.save(patron3);

        assertTrue(saved1.isPresent());
        assertTrue(saved2.isPresent());
        assertTrue(saved3.isPresent());

        assertEquals(patron1, saved1.get());
        assertEquals(patron2, saved2.get());
        assertEquals(patron3, saved3.get());
    }

    @Test
    void findById_ReturnsOptionalOfPatron() {
        persist();

        Optional<Patron> res1 = repo.findById(patron1.getId());
        Optional<Patron> res2 = repo.findById(patron2.getId());
        Optional<Patron> res3 = repo.findById(patron3.getId());

        assertTrue(res1.isPresent());
        assertTrue(res2.isPresent());
        assertTrue(res3.isPresent());

        assertEquals(patron1, res1.get());
        assertEquals(patron2, res2.get());
        assertEquals(patron3, res3.get());
    }

    @Test
    void findByCardId_ReturnsOptionalOfPatron() {
        persist();

        Optional<Patron> res1 = repo.findByCardId(patron1.getCardId());
        Optional<Patron> res2 = repo.findByCardId(patron2.getCardId());
        Optional<Patron> res3 = repo.findByCardId(patron3.getCardId());

        assertTrue(res1.isPresent());
        assertTrue(res2.isPresent());
        assertTrue(res3.isPresent());

        assertEquals(patron1, res1.get());
        assertEquals(patron2, res2.get());
        assertEquals(patron3, res3.get());
    }

    @Test
    void findByFullName_ReturnsListOfPatrons() {
        patron2.setFullName(patron1.getFullName());
        persist();

        List<Patron> res = repo.findByFullName(patron1.getFullName());

        assertEquals(2, res.size());
    }

    @Test
    void findByEmail_ReturnsOptionalOfPatron() {
        persist();

        Optional<Patron> res1 = repo.findByEmail(patron1.getEmail());
        Optional<Patron> res2 = repo.findByEmail(patron2.getEmail());
        Optional<Patron> res3 = repo.findByEmail(patron3.getEmail());

        assertTrue(res1.isPresent());
        assertTrue(res2.isPresent());
        assertTrue(res3.isPresent());

        assertEquals(patron1, res1.get());
        assertEquals(patron2, res2.get());
        assertEquals(patron3, res3.get());
    }

    @Test
    void findByPhone_ReturnsOptionalOfPatron() {
        persist();

        Optional<Patron> res1 = repo.findByPhone(patron1.getPhone());
        Optional<Patron> res2 = repo.findByPhone(patron2.getPhone());
        Optional<Patron> res3 = repo.findByPhone(patron3.getPhone());

        assertTrue(res1.isPresent());
        assertTrue(res2.isPresent());
        assertTrue(res3.isPresent());

        assertEquals(patron1, res1.get());
        assertEquals(patron2, res2.get());
        assertEquals(patron3, res3.get());
    }

    @Test
    void findByAddress_ReturnsOptionalOfPatron() {
        persist();

        Optional<Patron> res1 = repo.findByAddress(patron1.getAddress());
        Optional<Patron> res2 = repo.findByAddress(patron2.getAddress());
        Optional<Patron> res3 = repo.findByAddress(patron3.getAddress());

        assertTrue(res1.isPresent());
        assertTrue(res2.isPresent());
        assertTrue(res3.isPresent());

        assertEquals(patron1, res1.get());
        assertEquals(patron2, res2.get());
        assertEquals(patron3, res3.get());
    }

    @Test
    void findAll_ReturnsListOfAllPatrons() {
        persist();

        List<Patron> res = repo.findAll();

        assertEquals(3, res.size());
    }

    @Test
    void update_ReturnsOptionalOfUpdatedPatron() {
        String fullName = patron1.getFullName();

        String newFullName = "newFirstName newLastName";

        persist();

        patron1.setFullName(newFullName);

        Optional<Patron> res = repo.update(patron1);

        assertTrue(res.isPresent());

        assertNotEquals(fullName, res.get().getFullName());
    }

    @Test
    void deleteById_DeletesPatron() {
        persist();

        repo.deleteById(patron1.getId());
        repo.deleteById(patron2.getId());

        try (EntityManager em = emf.createEntityManager()) {
            assertNull(em.find(Patron.class, patron1.getId()));
            assertNull(em.find(Patron.class, patron2.getId()));
            assertNotNull(em.find(Patron.class, patron3.getId()));
        }
    }
}