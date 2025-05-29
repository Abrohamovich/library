package org.abrohamovich.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.entity.Category;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CategoryRepository implements GenericRepo<Category, Long> {
    private final EntityManagerFactory emf;

    @Override
    public Optional<Category> save(Category entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return Optional.ofNullable(entity);
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while saving category: {}", e.getMessage());
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while saving category: {}", e.getMessage());
        } finally {
            em.close();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Category> findById(long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(Category.class, id));
        } catch (PersistenceException e) {
            log.error("Persistence error while finding category by id {}: {}", id, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while finding category by id {}: {}", id, e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Category> findByName(String name) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Category> query = em.createQuery(
                    "SELECT c FROM Category c WHERE c.name = :name", Category.class);

            query.setParameter("name", name);

            return Optional.ofNullable(query.getSingleResult());
        } catch (PersistenceException e) {
            log.error("Persistence error while finding category by name - {}: {}", name, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while finding category by name - {}: {}", name, e.getMessage());
        }
        return Optional.empty();
    }

    public List<Category> findByNameLike(String name) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Category> query = em.createQuery(
                    "SELECT c FROM Category c WHERE c.name ILIKE :name", Category.class);

            query.setParameter("name", "%" + name + "%");

            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding category by name - {}: {}", name, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while finding category by name - {}: {}", name, e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public List<Category> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
        } catch (PersistenceException e) {
            log.error("Persistence error while finding all categories: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while finding all categories: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Category> update(Category entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Category updated = em.merge(entity);
            em.getTransaction().commit();
            return Optional.ofNullable(updated);
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while updating category with id {}: {}", entity.getId(), e.getMessage());
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while updating category with id {}: {}", entity.getId(), e.getMessage());
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
            Category category = em.find(Category.class, id);
            if (category != null) {
                em.remove(category);
            }
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            rollbackTransaction(em);
            log.error("Persistence error while deleting category with id {}: {}", id, e.getMessage());
        } catch (Exception e) {
            rollbackTransaction(em);
            log.error("Unexpected error while deleting category with id {}: {}", id, e.getMessage());
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
