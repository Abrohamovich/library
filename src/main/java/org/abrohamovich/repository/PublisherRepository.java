package org.abrohamovich.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.entity.Publisher;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PublisherRepository implements GenericRepo<Publisher, Long> {
    private final EntityManagerFactory emf;

    @Override
    public Optional<Publisher> save(Publisher entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return Optional.ofNullable(entity);
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while saving publisher", e);
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while saving publisher", e);
        } finally {
            em.close();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Publisher> findById(long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.of(em.find(Publisher.class, id));
        } catch (PersistenceException e) {
            log.error("Persistence error while finding publisher by id {}", id, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding publisher by id {}", id, e);
        }
        return Optional.empty();
    }

    public Optional<Publisher> findByName(String name) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Publisher> query = em.createQuery(
                    "SELECT s FROM Publisher s WHERE s.name = :name", Publisher.class);

            query.setParameter("name", name);

            return Optional.of(query.getSingleResult());
        } catch (PersistenceException e) {
            log.error("Persistence error while finding publisher by name {}", name, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding publisher by name {}", name, e);
        }
        return Optional.empty();
    }

    public Optional<Publisher> findByEmail(String email) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Publisher> query = em.createQuery(
                    "SELECT s FROM Publisher s WHERE s.email = :email", Publisher.class);

            query.setParameter("email", email);

            return Optional.of(query.getSingleResult());
        } catch (PersistenceException e) {
            log.error("Persistence error while finding publisher by email {}", email, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding publisher by email {}", email, e);
        }
        return Optional.empty();
    }

    public Optional<Publisher> findByWebsite(String website) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Publisher> query = em.createQuery(
                    "SELECT s FROM Publisher s WHERE s.website = :website", Publisher.class);

            query.setParameter("website", website);

            return Optional.of(query.getSingleResult());
        } catch (PersistenceException e) {
            log.error("Persistence error while finding publisher by website {}", website, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding publisher by website {}", website, e);
        }
        return Optional.empty();
    }

    public Optional<Publisher> findByAddress(String address) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Publisher> query = em.createQuery(
                    "SELECT s FROM Publisher s WHERE s.address = :address", Publisher.class);

            query.setParameter("address", address);

            return Optional.of(query.getSingleResult());
        } catch (PersistenceException e) {
            log.error("Persistence error while finding publisher by address {}", address, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding publisher by address {}", address, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Publisher> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT s FROM Publisher s", Publisher.class).getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding all publishers", e);
        } catch (Exception e) {
            log.error("Unexpected error while finding all publishers", e);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Publisher> update(Publisher entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Publisher updated = em.merge(entity);
            em.getTransaction().commit();
            return Optional.ofNullable(updated);
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while updating publisher with id {}", entity.getId(), e);
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while updating publisher with id {}", entity.getId(), e);
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
            Publisher publisher = em.find(Publisher.class, id);
            if (publisher != null) {
                em.remove(publisher);
            }
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while deleting publisher with id {}", id, e);
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while deleting publisher with id {}", id, e);
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
