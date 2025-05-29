package org.abrohamovich.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.entity.Patron;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PatronRepository implements GenericRepo<Patron, Long> {
    private final EntityManagerFactory emf;

    @Override
    public Optional<Patron> save(Patron entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return Optional.ofNullable(entity);
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while saving patron: {}", e.getMessage());
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while saving patron: {}", e.getMessage());
        } finally {
            em.close();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Patron> findById(long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(Patron.class, id));
        } catch (PersistenceException e) {
            log.error("Persistence error while finding patron by id {}: {}", id, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while finding patron by id {}: {}", id, e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Patron> findByCardId(String cardId) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Patron> query = em.createQuery(
                    "select p from Patron p where p.cardId = :cardId", Patron.class);

            query.setParameter("cardId", cardId);

            return Optional.ofNullable(query.getSingleResult());
        } catch (PersistenceException e) {
            log.error("Persistence error while finding patron by cardId {}: {}", cardId, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while finding patron by cardId {}: {}", cardId, e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Patron> findByBookId(long bookId) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Patron> query = em.createQuery(
                    "SELECT p FROM Patron p WHERE EXISTS (" +
                            "SELECT 1 FROM p.books bi WHERE bi.id = :bookId" +
                            ")", Patron.class);

            query.setParameter("bookId", bookId);

            return query.getResultStream().findFirst();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding patron by bookId {}: {}", bookId, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while finding patron by bookId {}: {}", bookId, e.getMessage());
        }
        return Optional.empty();
    }

    public List<Patron> findByFullName(String fullName) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Patron> query = em.createQuery(
                    "SELECT p FROM Patron p WHERE p.fullName ILIKE :fullName", Patron.class);

            query.setParameter("fullName", "%" + fullName + "%");

            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding patrons by full name {}: {}", fullName, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while finding patrons by full name {}: {}", fullName, e.getMessage());
        }
        return Collections.emptyList();
    }

    public Optional<Patron> findByEmail(String email) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Patron> query = em.createQuery(
                    "select p from Patron p where p.email = :email", Patron.class);

            query.setParameter("email", email);

            return Optional.ofNullable(query.getSingleResult());
        } catch (PersistenceException e) {
            log.error("Persistence error while finding patron by email {}: {}", email, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while finding patron by email {}: {}", email, e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Patron> findByPhone(String phone) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Patron> query = em.createQuery(
                    "select p from Patron p where p.phone = :phone", Patron.class);

            query.setParameter("phone", phone);

            return Optional.ofNullable(query.getSingleResult());
        } catch (PersistenceException e) {
            log.error("Persistence error while finding patron by phone {}: {}", phone, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while finding patron by phone {}: {}", phone, e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Patron> findByAddress(String address) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Patron> query = em.createQuery(
                    "select p from Patron p where p.address = :address", Patron.class);

            query.setParameter("address", address);

            return Optional.ofNullable(query.getSingleResult());
        } catch (PersistenceException e) {
            log.error("Persistence error while finding patron by address {}: {}", address, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while finding patron by address {}: {}", address, e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Patron> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT p FROM Patron p", Patron.class).getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding all patrons: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while finding all patrons: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Patron> update(Patron entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Patron updated = em.merge(entity);
            em.getTransaction().commit();
            return Optional.ofNullable(updated);
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while updating patron with id {}: {}", entity.getId(), e.getMessage());
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while updating patron with id {}: {}", entity.getId(), e.getMessage());
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
            Patron patron = em.find(Patron.class, id);
            if (patron != null) {
                em.remove(patron);
            }
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while deleting patron with id {}: {}", id, e.getMessage());
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while deleting patron with id {}: {}", id, e.getMessage());
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
