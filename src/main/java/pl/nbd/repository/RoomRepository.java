package pl.nbd.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import pl.nbd.model.Room;
import java.util.List;

public class RoomRepository implements Repository<Room> {
    private EntityManagerFactory entityManagerFactory;

    public RoomRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void create(Room room) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(room);
        entityManager.getTransaction().commit();
    }

    @Override
    public Room read(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(Room.class, id);
    }

    @Override
    public void update(Room room) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(room);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Room room) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Room managedRoom = entityManager.find(Room.class, room.getRoomNumber());
        if (managedRoom != null) {
            entityManager.remove(managedRoom);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public List<Room> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Room> query = criteriaBuilder.createQuery(Room.class);
        Root<Room> root = query.from(Room.class);
        query.select(root);

        return entityManager.createQuery(query).getResultList();
    }
}
