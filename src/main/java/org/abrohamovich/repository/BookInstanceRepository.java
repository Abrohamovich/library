package org.abrohamovich.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.entity.BookInstance;
import org.abrohamovich.entity.Format;
import org.abrohamovich.entity.Status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class BookInstanceRepository implements GenericRepo<BookInstance, Long> {
    private final EntityManagerFactory emf;

    @Override
    public Optional<BookInstance> save(BookInstance entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return Optional.ofNullable(entity);
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while saving bookInstance entity", e);
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while saving bookInstance entity", e);
        } finally {
            em.close();
        }
        return Optional.empty();
    }

    @Override
    public Optional<BookInstance> findById(long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(BookInstance.class, id));
        } catch (PersistenceException e) {
            log.error("Persistence error while finding bookInstance by id {}", id, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding bookInstance by id {}", id, e);
        }
        return Optional.empty();
    }

    public List<BookInstance> findByBookId(long bookId) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<BookInstance> query = em.createQuery(
                    "SELECT bi FROM BookInstance bi WHERE bi.book.id = :bookId", BookInstance.class);

            query.setParameter("bookId", bookId);

            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding bookInstances by bookId {}", bookId, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding bookInstance by bookId {}", bookId, e);
        }
        return Collections.emptyList();
    }

    public List<BookInstance> findByPublisherId(long publisherId) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<BookInstance> query = em.createQuery(
                    "SELECT bi FROM BookInstance bi WHERE bi.publisher.id = :publisherId", BookInstance.class);

            query.setParameter("publisherId", publisherId);

            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding bookInstances by publisherId {}", publisherId, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding bookInstances by publisherId {}", publisherId, e);
        }
        return Collections.emptyList();
    }

    public List<BookInstance> findByStatus(Status status) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<BookInstance> query = em.createQuery(
                    "SELECT bi FROM BookInstance bi WHERE bi.status = :status", BookInstance.class);

            query.setParameter("status", status);

            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding bookInstances by status {}", status, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding bookInstances by status {}", status, e);
        }
        return Collections.emptyList();
    }

    public List<BookInstance> findByFormat(Format format) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<BookInstance> query = em.createQuery(
                    "SELECT bi FROM BookInstance bi WHERE bi.format = :format", BookInstance.class);

            query.setParameter("format", format);

            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding bookInstances by format {}", format, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding bookInstances by format {}", format, e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<BookInstance> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<BookInstance> query = em.createQuery(
                    "SELECT i FROM BookInstance i", BookInstance.class);
            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding all bookInstances", e);
        } catch (Exception e) {
            log.error("Unexpected error while finding all bookInstances", e);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<BookInstance> update(BookInstance entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            BookInstance updated = em.merge(entity);
            em.getTransaction().commit();
            return Optional.ofNullable(updated);
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while updating bookInstance with id {}", entity.getId(), e);
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while updating bookInstance with id {}", entity.getId(), e);
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
            BookInstance bookInstance = em.find(BookInstance.class, id);
            if (bookInstance != null) {
                em.remove(bookInstance);
            }
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while deleting bookInstance with id {}", id, e);
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while deleting bookInstance with id {}", id, e);
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
