package org.abrohamovich.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class EntityManagerFactoryProvider {

    private static EntityManagerFactory entityManagerFactory;

    private EntityManagerFactoryProvider() {
    }

    public static void initialize(String persistenceUnitName) {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            Map<String, String> jpaConfig = new HashMap<>();
            jpaConfig.put("jakarta.persistence.jdbc.url", String.format("jdbc:postgresql://localhost:5432/%s", System.getenv("DB_NAME")));
            jpaConfig.put("jakarta.persistence.jdbc.user", System.getenv("DB_USERNAME"));
            jpaConfig.put("jakarta.persistence.jdbc.password", System.getenv("DB_PASSWORD"));

            entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, jpaConfig);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            throw new IllegalStateException("EntityManagerFactory has not been initialized or is closed.");
        }
        return entityManagerFactory;
    }

    public static void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            System.out.println("EntityManagerFactory closed.");
        }
    }
}
