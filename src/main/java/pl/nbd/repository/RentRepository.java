package pl.nbd.repository;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import pl.nbd.model.Client;
import pl.nbd.model.Rent;
import pl.nbd.model.Room;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RentRepository implements Repository<Rent> {
    private EntityManagerFactory entityManagerFactory;

    public RentRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }


    public void createReservation(long id, Client client, Room room, LocalDateTime startDate) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Map<String, Object> properties = new HashMap<>();
            properties.put("javax.persistence.lock.timeout", 0);
            entityManager.getTransaction().begin();
            Room managedRoom = entityManager.find(Room.class, room.getRoomNumber(), LockModeType.PESSIMISTIC_WRITE, properties);
            Rent rent = new Rent(id, client, managedRoom, startDate);
            entityManager.persist(rent);
            entityManager.getTransaction().commit();
        }
        catch (PessimisticLockException | LockTimeoutException e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Rent rent) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(rent);
        entityManager.getTransaction().commit();
    }

    @Override
    public Rent read(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(Rent.class, id);
    }

    @Override
    public void update(Rent rent) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(rent);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Rent rent) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Rent managedRent = entityManager.find(Rent.class, rent.getId());
        if (managedRent != null) {
            entityManager.remove(managedRent);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public List<Rent> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rent> query = criteriaBuilder.createQuery(Rent.class);
        Root<Rent> root = query.from(Rent.class);
        query.select(root);

        return entityManager.createQuery(query).getResultList();
    }
}
