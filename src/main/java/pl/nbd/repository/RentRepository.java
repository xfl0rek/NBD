package pl.nbd.repository;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import pl.nbd.model.Client;
import pl.nbd.model.Rent;
import pl.nbd.model.Room;

import java.time.LocalDateTime;
import java.util.List;

public class RentRepository implements Repository<Rent> {
    private EntityManager entityManager;

    public RentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public void createReservation(long id, Client client, Room room, LocalDateTime startDate) {
        entityManager.getTransaction().begin();
        Room managedRoom = entityManager.find(Room.class, room.getRoomNumber());
        entityManager.lock(managedRoom, LockModeType.PESSIMISTIC_WRITE);
        Rent rent = new Rent(id, client, managedRoom, startDate);
        entityManager.persist(rent);
        entityManager.getTransaction().commit();
    }

    @Override
    public void create(Rent rent) {
        entityManager.getTransaction().begin();
        entityManager.persist(rent);
        entityManager.getTransaction().commit();
    }

    @Override
    public Rent read(long id) {
        return entityManager.find(Rent.class, id);
    }

    @Override
    public void update(Rent rent) {
        entityManager.getTransaction().begin();
        entityManager.merge(rent);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Rent rent) {
        entityManager.getTransaction().begin();
        Rent managedRent = entityManager.find(Rent.class, rent.getId());
        if (managedRent != null) {
            entityManager.remove(managedRent);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public List<Rent> getAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rent> query = criteriaBuilder.createQuery(Rent.class);
        Root<Rent> root = query.from(Rent.class);
        query.select(root);

        return entityManager.createQuery(query).getResultList();
    }
}
