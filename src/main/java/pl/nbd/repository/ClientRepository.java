package pl.nbd.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import pl.nbd.model.Client;
import java.util.List;

public class ClientRepository implements Repository<Client> {
    private EntityManagerFactory entityManagerFactory;

    public ClientRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void create(Client client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(client);
        entityManager.getTransaction().commit();
    }

    @Override
    public Client read(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(Client.class, id);
    }

    @Override
    public void update(Client client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(client);
        entityManager.getTransaction().commit();
   }

    @Override
    public void delete(Client client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Client managedClient = entityManager.find(Client.class, client.getPersonalID());
        if (managedClient != null) {
            entityManager.remove(managedClient);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public List<Client> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> query = criteriaBuilder.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }
}
