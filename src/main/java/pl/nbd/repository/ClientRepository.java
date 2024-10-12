package pl.nbd.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Criteria;
import pl.nbd.model.Client;
import pl.nbd.model.Rent;

import java.util.ArrayList;
import java.util.List;

public class ClientRepository implements Repository<Client> {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    @Override
    public void create(Client client) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(client);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public Client read(long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.find(Client.class, id);
        }
    }

    @Override
    public void update(Client client) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.merge(client);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public void delete(Client client) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            Client managedClient = entityManager.find(Client.class, client.getPersonalID());
            if (managedClient != null) {
                entityManager.remove(managedClient);
            }
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public List<Client> getAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Client> query = criteriaBuilder.createQuery(Client.class);
            Root<Client> root = query.from(Client.class);
            query.select(root);

            return entityManager.createQuery(query).getResultList();
        }
    }
}
