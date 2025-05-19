package org.abrohamovich.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.abrohamovich.entity.Publisher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PublisherRepositoryTest {
    private PublisherRepository repo;
    private EntityManagerFactory emf;
    private Publisher publisher1;
    private Publisher publisher2;
    private Publisher publisher3;

    @BeforeEach
    void setUp() {

        emf = Persistence.createEntityManagerFactory("test-persistence-unit");
        repo = new PublisherRepository(emf);

        publisher1 = Publisher.builder()
                .name("Penguin Random House")
                .foundationDate(LocalDate.of(1927, 7, 1))
                .address("1745 Broadway, New York, NY 10019, USA")
                .email("contact@penguinrandomhouse.com")
                .website("https://www.penguinrandomhouse.com")
                .build();
        publisher2 = Publisher.builder()
                .name("HarperCollins")
                .foundationDate(LocalDate.of(1989, 8, 1))
                .address("195 Broadway, New York, NY 10007, USA")
                .email("info@harpercollins.com")
                .website("https://www.harpercollins.com")
                .build();
        publisher3 = Publisher.builder()
                .name("Macmillan Publishers")
                .foundationDate(LocalDate.of(1843, 10, 1))
                .address("120 Broadway, New York, NY 10271, USA")
                .email("support@macmillan.com")
                .website("https://us.macmillan.com")
                .build();
    }

    @AfterEach
    public void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    void persistPublisher() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(publisher1);
            em.persist(publisher2);
            em.persist(publisher3);
            em.getTransaction().commit();
        }
    }

    @Test
    void save_ReturnsOptionalOfSavedPublisher() {
        Optional<Publisher> saved1 = repo.save(publisher1);
        Optional<Publisher> saved2 = repo.save(publisher2);
        Optional<Publisher> saved3 = repo.save(publisher3);

        assertTrue(saved1.isPresent());
        assertTrue(saved2.isPresent());
        assertTrue(saved3.isPresent());

        assertEquals(publisher1, saved1.get());
        assertEquals(publisher2, saved2.get());
        assertEquals(publisher3, saved3.get());
    }

    @Test
    void findById_ReturnsOptionalOfPublisher() {
        persistPublisher();

        Optional<Publisher> found1 = repo.findById(publisher1.getId());
        Optional<Publisher> found2 = repo.findById(publisher2.getId());
        Optional<Publisher> found3 = repo.findById(publisher3.getId());

        assertTrue(found1.isPresent());
        assertTrue(found2.isPresent());
        assertTrue(found3.isPresent());

        assertEquals(publisher1, found1.get());
        assertEquals(publisher2, found2.get());
        assertEquals(publisher3, found3.get());
    }

    @Test
    void findByName_ReturnsOptionalOfPublisher() {
        persistPublisher();

        Optional<Publisher> found1 = repo.findByName(publisher1.getName());
        Optional<Publisher> found2 = repo.findByName(publisher2.getName());
        Optional<Publisher> found3 = repo.findByName(publisher3.getName());

        assertTrue(found1.isPresent());
        assertTrue(found2.isPresent());
        assertTrue(found3.isPresent());

        assertEquals(publisher1, found1.get());
        assertEquals(publisher2, found2.get());
        assertEquals(publisher3, found3.get());
    }

    @Test
    void findByEmail_ReturnsOptionalOfPublisher() {
        persistPublisher();

        Optional<Publisher> found1 = repo.findByEmail(publisher1.getEmail());
        Optional<Publisher> found2 = repo.findByEmail(publisher2.getEmail());
        Optional<Publisher> found3 = repo.findByEmail(publisher3.getEmail());

        assertTrue(found1.isPresent());
        assertTrue(found2.isPresent());
        assertTrue(found3.isPresent());

        assertEquals(publisher1, found1.get());
        assertEquals(publisher2, found2.get());
        assertEquals(publisher3, found3.get());
    }

    @Test
    void findByWebsite_ReturnsOptionalOfPublisher() {
        persistPublisher();

        Optional<Publisher> found1 = repo.findByWebsite(publisher1.getWebsite());
        Optional<Publisher> found2 = repo.findByWebsite(publisher2.getWebsite());
        Optional<Publisher> found3 = repo.findByWebsite(publisher3.getWebsite());

        assertTrue(found1.isPresent());
        assertTrue(found2.isPresent());
        assertTrue(found3.isPresent());

        assertEquals(publisher1, found1.get());
        assertEquals(publisher2, found2.get());
        assertEquals(publisher3, found3.get());
    }

    @Test
    void findByAddress_ReturnsOptionalOfPublisher() {
        persistPublisher();

        Optional<Publisher> found1 = repo.findByAddress(publisher1.getAddress());
        Optional<Publisher> found2 = repo.findByAddress(publisher2.getAddress());
        Optional<Publisher> found3 = repo.findByAddress(publisher3.getAddress());

        assertTrue(found1.isPresent());
        assertTrue(found2.isPresent());
        assertTrue(found3.isPresent());

        assertEquals(publisher1, found1.get());
        assertEquals(publisher2, found2.get());
        assertEquals(publisher3, found3.get());
    }

    @Test
    void findAll_ReturnsListOfAllPublishers() {
        persistPublisher();

        List<Publisher> publishers = repo.findAll();

        assertEquals(3, publishers.size());
        assertArrayEquals(List.of(publisher1, publisher2, publisher3).toArray(), publishers.toArray());
    }

    @Test
    void update_ReturnsOptionalOfUpdatedPublisher() {
        String name = "Penguin Random House";
        LocalDate foundationDate = LocalDate.of(1927, 7, 1);
        String address = "1745 Broadway, New York, NY 10019, USA";

        String newName = "newname";
        LocalDate newFoundationDate = LocalDate.of(1989, 8, 1);
        String newAddress = "York, UK";

        persistPublisher();

        publisher1.setName(newName);
        publisher1.setFoundationDate(newFoundationDate);
        publisher1.setAddress(newAddress);

        Optional<Publisher> updatedPublisher = repo.update(publisher1);

        assertTrue(updatedPublisher.isPresent());
        assertNotEquals(name, updatedPublisher.get().getName());
        assertNotEquals(address, updatedPublisher.get().getAddress());
        assertNotEquals(foundationDate, updatedPublisher.get().getFoundationDate());
    }

    @Test
    void deleteById_DeletesPublisher() {
        persistPublisher();

        repo.deleteById(publisher1.getId());

        try (EntityManager em = emf.createEntityManager()) {
            assertNull(em.find(Publisher.class, publisher1.getId()));
            assertNotNull(em.find(Publisher.class, publisher2.getId()));
            assertNotNull(em.find(Publisher.class, publisher3.getId()));
        }
    }
}