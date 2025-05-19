package org.abrohamovich.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.entity.Book;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class BookRepository implements GenericRepo<Book, Long> {
    private final EntityManagerFactory emf;

    @Override
    public Optional<Book> save(Book entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return Optional.ofNullable(entity);
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while saving book", e);
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while saving book", e);
        } finally {
            em.close();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> findById(long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(Book.class, id));
        } catch (PersistenceException e) {
            log.error("Persistence error while finding book by id {}", id, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding book by id {}", id, e);
        }
        return Optional.empty();
    }

    public Optional<Book> findByIsbn(String isbn) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Book> query = em.createQuery(
                    "SELECT b FROM Book b WHERE b.isbn = :isbn", Book.class);
            query.setParameter("isbn", isbn);
            return Optional.ofNullable(query.getSingleResult());
        } catch (PersistenceException e) {
            log.error("Persistence error while finding book by ISBN {}", isbn, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding book by ISBN {}", isbn, e);
        }
        return Optional.empty();
    }

    public List<Book> findByAuthorIds(List<Long> authorIds) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Book> query = em.createQuery(
                    "SELECT b FROM Book b " +
                            "JOIN b.authors a " +
                            "WHERE a.id IN :authorIds " +
                            "GROUP BY b " +
                            "HAVING COUNT(DISTINCT a.id) = :authorCount", Book.class);
            query.setParameter("authorIds", authorIds);
            query.setParameter("authorCount", (long) authorIds.size());
            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding books by author IDs {}", authorIds, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding books by author IDs {}", authorIds, e);
        }
        return Collections.emptyList();
    }

    public List<Book> findByGenreIds(List<Long> genreIds) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Book> query = em.createQuery(
                    "SELECT b FROM Book b " +
                            "JOIN b.genres g " +
                            "WHERE g.id IN :genreIds " +
                            "GROUP BY b " +
                            "HAVING COUNT(DISTINCT g.id) = :genreCount", Book.class);
            query.setParameter("genreIds", genreIds);
            query.setParameter("genreCount", (long) genreIds.size());
            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding books by genre IDs {}", genreIds, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding books by genre IDs {}", genreIds, e);
        }
        return Collections.emptyList();
    }

    public List<Book> findByCategoryIds(List<Long> categoryIds) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Book> query = em.createQuery(
                    "SELECT b FROM Book b " +
                            "JOIN b.categories c " +
                            "WHERE c.id IN :categoryIds " +
                            "GROUP BY b " +
                            "HAVING COUNT(DISTINCT c.id) = :categoryCount", Book.class);
            query.setParameter("categoryIds", categoryIds);
            query.setParameter("categoryCount", (long) categoryIds.size());
            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding books by category IDs {}", categoryIds, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding books by category IDs {}", categoryIds, e);
        }
        return Collections.emptyList();
    }

    public List<Book> findByLanguage(String language) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Book> query = em.createQuery(
                    "SELECT b FROM Book b WHERE b.language = :language", Book.class);
            query.setParameter("language", language);
            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding books by language '{}'", language, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding books by language '{}'", language, e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Book> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b", Book.class);
            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding all books", e);
        } catch (Exception e) {
            log.error("Unexpected error while finding all books", e);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Book> update(Book entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Book updated = em.merge(entity);
            em.getTransaction().commit();
            return Optional.ofNullable(updated);
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while updating entity with id {}", entity.getId(), e);
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while updating entity with id {}", entity.getId(), e);
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
            Book book = em.find(Book.class, id);
            if (book != null) {
                em.remove(book);
            }
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while deleting book with id {}", id, e);
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while deleting book with id {}", id, e);
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
