package org.abrohamovich.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.entity.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class GenreRepository implements GenericRepo<Genre, Long> {
    private final EntityManagerFactory emf;

    @Override
    public Optional<Genre> save(Genre entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return Optional.ofNullable(entity);
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while saving genre", e);
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while saving genre", e);
        } finally {
            em.close();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Genre> findById(long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(Genre.class, id));
        } catch (PersistenceException e) {
            log.error("Persistence error while finding genre by id {}", id, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding genre by id {}", id, e);
        }
        return Optional.empty();
    }

    public Optional<Genre> findByName(String name) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Genre> query = em.createQuery(
                    "SELECT s FROM Genre s WHERE s.name = :name", Genre.class);

            query.setParameter("name", name);

            return Optional.ofNullable(query.getSingleResult());
        } catch (PersistenceException e) {
            log.error("Persistence error while finding genre by name {}", name, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding genre by name {}", name, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Genre> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT g FROM Genre g", Genre.class).getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding all genres", e);
        } catch (Exception e) {
            log.error("Unexpected error while finding all genres", e);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Genre> update(Genre entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Genre updated = em.merge(entity);
            em.getTransaction().commit();
            return Optional.ofNullable(updated);
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while updating genre with id {}", entity.getId(), e);
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while updating genre with id {}", entity.getId(), e);
        } finally {
            em.close();
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Genre genre = em.find(Genre.class, id);
            if (genre != null) {
                em.remove(genre);
            }
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while deleting genre with id {}", id, e);
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while deleting genre with id {}", id, e);
        } finally {
            em.close();
        }
    }

    private void rollbackTransaction(EntityManager em) {
        if (em.getTransaction().isActive()) {
            try {
                em.getTransaction().rollback();
            } catch (Exception ex) {
                log.error("Failed to rollback transaction", ex);
            }
        }
    }
}
