package org.abrohamovich.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.abrohamovich.entity.Genre;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GenreRepositoryTest {
    private GenreRepository repo;
    private EntityManagerFactory emf;
    private Genre genre1;
    private Genre genre2;
    private Genre genre3;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("test-persistence-unit");
        repo = new GenreRepository(emf);

        genre1 = Genre.builder()
                .name("Thriller")
                .description("Genre characterized by suspense, excitement, and tension")
                .build();
        genre2 = Genre.builder()
                .name("Romance")
                .description("Genre centered around romantic relationships between characters")
                .build();
        genre3 = Genre.builder()
                .name("Horror")
                .description("Genre intended to scare, unsettle, or horrify the reader")
                .build();
    }

    @AfterEach
    public void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    void persist() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(genre1);
            em.persist(genre2);
            em.persist(genre3);
            em.getTransaction().commit();
        }
    }

    @Test
    void save_ReturnsOptionalOfGenre() {
        Optional<Genre> saved1 = repo.save(genre1);
        Optional<Genre> saved2 = repo.save(genre2);
        Optional<Genre> saved3 = repo.save(genre3);

        assertTrue(saved1.isPresent());
        assertTrue(saved2.isPresent());
        assertTrue(saved3.isPresent());

        assertEquals(genre1, saved1.get());
        assertEquals(genre2, saved2.get());
        assertEquals(genre3, saved3.get());
    }

    @Test
    void findById_ReturnsOptionalOfGenre() {
        persist();

        Optional<Genre> found1 = repo.findById(genre1.getId());
        Optional<Genre> found2 = repo.findById(genre2.getId());
        Optional<Genre> found3 = repo.findById(genre3.getId());

        assertTrue(found1.isPresent());
        assertTrue(found2.isPresent());
        assertTrue(found3.isPresent());

        assertEquals(genre1, found1.get());
        assertEquals(genre2, found2.get());
        assertEquals(genre3, found3.get());
    }

    @Test
    void findByName_ReturnsOptionalOfGenre() {
        persist();

        Optional<Genre> found1 = repo.findByName(genre1.getName());
        Optional<Genre> found2 = repo.findByName(genre2.getName());
        Optional<Genre> found3 = repo.findByName(genre3.getName());

        assertTrue(found1.isPresent());
        assertTrue(found2.isPresent());
        assertTrue(found3.isPresent());

        assertEquals(genre1, found1.get());
        assertEquals(genre2, found2.get());
        assertEquals(genre3, found3.get());
    }

    @Test
    void findAll_ReturnsListOfAllGenres() {
        persist();

        List<Genre> allGenres = repo.findAll();

        assertArrayEquals(List.of(genre1, genre2, genre3).toArray(), allGenres.toArray());
    }

    @Test
    void update_ReturnsOptionalOfUpdatedGenre() {
        String name = genre1.getName();
        String description = genre1.getDescription();

        String newName = "Dystopian";
        String newDescription = "Genre depicting an imagined, oppressive, and often futuristic society";

        persist();

        genre1.setName(newName);
        genre2.setDescription(newDescription);

        Optional<Genre> updatedGenre = repo.update(genre1);

        assertTrue(updatedGenre.isPresent());

        assertNotEquals(name, updatedGenre.get().getName());
        assertEquals(description, updatedGenre.get().getDescription());
    }

    @Test
    void deleteById_DeletesGenreWithGivenId() {
        persist();

        repo.deleteById(genre1.getId());

        try (EntityManager em = emf.createEntityManager()) {
            assertNull(em.find(Genre.class, genre1.getId()));
            assertNotNull(em.find(Genre.class, genre2.getId()));
            assertNotNull(em.find(Genre.class, genre3.getId()));
        }
    }
}