package pl.nbd.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Criteria;
import pl.nbd.model.Client;

import java.util.ArrayList;

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



//    @Override
//    public ArrayList<Client> findBy(CriteriaQuery criteria) {
//        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
//
//
//            return entityManager.createQuery("SELECT c FROM Client c", Client.class).getResultList();
//        }
//    }
}
