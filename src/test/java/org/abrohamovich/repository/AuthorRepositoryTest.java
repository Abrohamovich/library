package org.abrohamovich.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.abrohamovich.entity.Author;
import org.abrohamovich.entity.Sex;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthorRepositoryTest {
    private AuthorRepository repo;
    private EntityManagerFactory emf;
    private Author author1;
    private Author author2;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("test-persistence-unit");
        repo = new AuthorRepository(emf);
        author1 = Author.builder()
                .fullName("Jane Austen")
                .dateOfBirth(LocalDate.of(1775, 12, 15))
                .sex(Sex.FEMALE).nationality("British")
                .build();

        author2 = Author.builder()
                .fullName("Gabriel García Márquez")
                .dateOfBirth(LocalDate.of(1927, 3, 6))
                .sex(Sex.MALE).nationality("Colombian")
                .build();
    }

    @AfterEach
    public void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    void persistAuthors() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(author1);
            em.persist(author2);
            em.getTransaction().commit();
        }
    }

    @Test
    void save_ReturnsSavedAuthor() {
        Optional<Author> saved1 = repo.save(author1);
        Optional<Author> saved2 = repo.save(author2);

        assertTrue(saved1.isPresent());
        assertTrue(saved2.isPresent());

        assertNotEquals(0, saved1.get().getId());
        assertNotEquals(0, saved2.get().getId());
    }

    @Test
    void save_ReturnsEmpty() {
        author1.setFullName(null);

        Optional<Author> saved1 = repo.save(author1);

        assertTrue(saved1.isEmpty());
    }

    @Test
    void findById_ReturnsAuthor() {
        persistAuthors();

        Optional<Author> found1 = repo.findById(author1.getId());
        Optional<Author> found2 = repo.findById(author2.getId());

        assertTrue(found1.isPresent());
        assertTrue(found2.isPresent());

        assertEquals(author1, found1.get());
        assertEquals(author2, found2.get());
    }

    @Test
    void findById_ReturnsEmpty() {
        Optional<Author> found = repo.findById(999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void findByFullName_ReturnsSingleAuthor() {
        persistAuthors();

        List<Author> authors = repo.findByFullName(author1.getFullName());

        assertEquals(1, authors.size());
        assertEquals(author1, authors.getFirst());
    }

    @Test
    void findByFullName_ReturnsMultipleAuthors() {
        Author newAuthor = Author.builder()
                .fullName("Gabriel García Márquez")
                .dateOfBirth(LocalDate.of(1958, 10, 1))
                .sex(Sex.MALE).nationality("Honduras")
                .build();

        persistAuthors();

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(newAuthor);
            em.getTransaction().commit();
        }

        List<Author> authors = repo.findByFullName(author2.getFullName());

        assertEquals(2, authors.size());
    }

    @Test
    void findByFullName_ReturnsEmptyList() {
        List<Author> authors = repo.findByFullName("smth smth");

        assertEquals(0, authors.size());
    }

    @Test
    void findBySex_ReturnsAuthors() {
        persistAuthors();

        List<Author> authors = repo.findBySex(Sex.MALE);

        assertEquals(1, authors.size());
        assertEquals(author2, authors.getFirst());
    }

    @Test
    void findBySex_ReturnsMultipleAuthors() {
        Author newAuthor = Author.builder()
                .fullName("Gabriel García-Márquez")
                .dateOfBirth(LocalDate.of(1958, 10, 1))
                .sex(Sex.MALE).nationality("Honduras")
                .build();

        persistAuthors();

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(newAuthor);
            em.getTransaction().commit();
        }

        List<Author> authors = repo.findBySex(Sex.MALE);

        assertEquals(2, authors.size());
    }

    @Test
    void findAll_ReturnsAllAuthors() {
        persistAuthors();

        List<Author> authors = repo.findAll();

        assertEquals(2, authors.size());
    }

    @Test
    void findAll_ReturnsEmptyList() {
        List<Author> authors = repo.findAll();

        assertEquals(0, authors.size());
    }

    @Test
    void update_ReturnsUpdatedAuthor() {
        persistAuthors();

        author1.setNationality("American");

        Optional<Author> updated = repo.update(author1);

        try (EntityManager em = emf.createEntityManager()) {
            assertEquals("American", em.find(Author.class, author1.getId()).getNationality());
        }
    }

    @Test
    void update_ReturnsEmpty() {
        persistAuthors();

        author1.setNationality(null);

        Optional<Author> updated = repo.update(author1);

        assertTrue(updated.isEmpty());
    }

    @Test
    void deleteById_RemovesAuthors() {
        persistAuthors();

        repo.deleteById(author1.getId());
        repo.deleteById(author2.getId());

        try (EntityManager em = emf.createEntityManager()) {
            assertNull(em.find(Author.class, author1.getId()));
            assertNull(em.find(Author.class, author2.getId()));
        }
    }
}