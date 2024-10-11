package pl.nbd.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import pl.nbd.model.Rent;

public class RentRepository implements Repository<Rent> {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    @Override
    public void create(Rent rent) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(rent);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public Rent read(long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.find(Rent.class, id);
        }
    }

    @Override
    public void update(Rent rent) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.merge(rent);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public void delete(Rent rent) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            Rent managedRent = entityManager.find(Rent.class, rent.getId());
            if (managedRent != null) {
                entityManager.remove(managedRent);
            }
            entityManager.getTransaction().commit();
        }
    }
}
