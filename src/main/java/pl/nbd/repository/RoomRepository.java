package pl.nbd.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import pl.nbd.model.Rent;
import pl.nbd.model.Room;

import java.util.List;

public class RoomRepository implements Repository<Room> {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    @Override
    public void create(Room room) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(room);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public Room read(long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.find(Room.class, id);
        }
    }

    @Override
    public void update(Room room) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.merge(room);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public void delete(Room room) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            Room managedRoom = entityManager.find(Room.class, room.getRoomNumber());
            if (managedRoom != null) {
                entityManager.remove(managedRoom);
            }
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public List<Room> getAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Room> query = criteriaBuilder.createQuery(Room.class);
            Root<Room> root = query.from(Room.class);
            query.select(root);

            return entityManager.createQuery(query).getResultList();
        }
    }
}
