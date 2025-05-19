package org.abrohamovich.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.entity.Author;
import org.abrohamovich.entity.Sex;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class AuthorRepository implements GenericRepo<Author, Long> {
    private final EntityManagerFactory emf;

    @Override
    public Optional<Author> save(Author entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return Optional.ofNullable(entity);
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while saving author", e);
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while saving author", e);
        } finally {
            em.close();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Author> findById(long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(Author.class, id));
        } catch (Exception e) {
            log.error("Unexpected error while finding author by id {}", id, e);
        }
        return Optional.empty();
    }

    public List<Author> findByFullName(String fullName) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Author> query = em.createQuery(
                    "SELECT a FROM Author a WHERE a.fullName ILIKE :fullName",
                    Author.class
            );
            query.setParameter("fullName", "%" + fullName + "%");
            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding authors by full name - {}", fullName, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding authors by full name - {}", fullName, e);
        }
        return Collections.emptyList();
    }

    public List<Author> findBySex(Sex sex) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Author> query = em.createQuery(
                    "SELECT a FROM Author a WHERE a.sex = :sex",
                    Author.class
            );
            query.setParameter("sex", sex);
            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding authors by sex - {}", sex, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding authors by sex - {}", sex, e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Author> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT a FROM Author a", Author.class).getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding all authors", e);
        } catch (Exception e) {
            log.error("Unexpected error while finding all authors", e);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Author> update(Author entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Author updated = em.merge(entity);
            em.getTransaction().commit();
            return Optional.ofNullable(updated);
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while updating author with id {}", entity.getId(), e);
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while updating author with id {}", entity.getId(), e);
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
            Author author = em.find(Author.class, id);
            if (author != null) {
                em.remove(author);
            }
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while deleting author with id {}", id, e);
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while deleting author with id {}", id, e);
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
