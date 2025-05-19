package org.abrohamovich.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.abrohamovich.entity.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRepositoryTest {
    private CategoryRepository repo;
    private EntityManagerFactory emf;
    private Category category1;
    private Category category2;
    private Category category3;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("test-persistence-unit");
        repo = new CategoryRepository(emf);

        category1 = Category.builder()
                .name("Fiction")
                .description("Narrative literature created from the imagination, not presented as fact.")
                .build();

        category2 = Category.builder()
                .name("Science")
                .description("Books that explain scientific concepts and discoveries.")
                .build();

        category3 = Category.builder()
                .name("History")
                .description("Books that explore past events and historical figures.")
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
            em.persist(category1);
            em.persist(category2);
            em.persist(category3);
            em.getTransaction().commit();
        }
    }

    @Test
    void save_ReturnsOptionalOfCategory() {
        Optional<Category> saved1 = repo.save(category1);
        Optional<Category> saved2 = repo.save(category2);
        Optional<Category> saved3 = repo.save(category3);

        assertTrue(saved1.isPresent());
        assertTrue(saved2.isPresent());
        assertTrue(saved3.isPresent());

        assertEquals(category1, saved1.get());
        assertEquals(category2, saved2.get());
        assertEquals(category3, saved3.get());
    }

    @Test
    void findById_ReturnsOptionalOfCategory() {
        persist();

        Optional<Category> saved1 = repo.findById(category1.getId());
        Optional<Category> saved2 = repo.findById(category2.getId());
        Optional<Category> saved3 = repo.findById(category3.getId());

        assertTrue(saved1.isPresent());
        assertTrue(saved2.isPresent());
        assertTrue(saved3.isPresent());

        assertEquals(category1, saved1.get());
        assertEquals(category2, saved2.get());
        assertEquals(category3, saved3.get());
    }

    @Test
    void findByName_ReturnsOptionalOfCategory() {
        persist();

        Optional<Category> saved1 = repo.findByName(category1.getName());
        Optional<Category> saved2 = repo.findByName(category2.getName());
        Optional<Category> saved3 = repo.findByName(category3.getName());

        assertTrue(saved1.isPresent());
        assertTrue(saved2.isPresent());
        assertTrue(saved3.isPresent());

        assertEquals(category1, saved1.get());
        assertEquals(category2, saved2.get());
        assertEquals(category3, saved3.get());
    }

    @Test
    void findAll_ReturnsListOfAllCategories() {
        persist();

        List<Category> saved = repo.findAll();

        assertArrayEquals(List.of(category1, category2, category3).toArray(), saved.toArray());
    }

    @Test
    void update_ReturnsOptionalOfUpdatedCategory() {
        String name = category1.getName();
        String description = category1.getDescription();

        String newName = "Fantasy";
        String newDescription = "Books set in fictional universes with magical or supernatural elements";

        persist();

        category1.setName(newName);
        category2.setDescription(newDescription);

        Optional<Category> updatedCategory = repo.update(category1);

        assertTrue(updatedCategory.isPresent());

        assertNotEquals(name, updatedCategory.get().getName());
        assertEquals(description, updatedCategory.get().getDescription());
    }

    @Test
    void deleteById_DeletesCategory() {
        persist();

        repo.deleteById(category1.getId());

        try (EntityManager em = emf.createEntityManager()) {
            assertNull(em.find(Category.class, category1.getId()));
            assertNotNull(em.find(Category.class, category2.getId()));
            assertNotNull(em.find(Category.class, category3.getId()));
        }
    }
}