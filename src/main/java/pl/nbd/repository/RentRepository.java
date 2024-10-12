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
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");


    public void createReservation(Client client, Room room, LocalDateTime startDate) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
//            entityManager.lock(room, LockModeType.PESSIMISTIC_WRITE);
            Rent rent = new Rent(client, room, startDate);
            entityManager.persist(rent);
            entityManager.getTransaction().commit();
        }
    }
//
//    public Rent endRent(long id, LocalDateTime endDate) {
//        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
//            entityManager.getTransaction().begin();
//            Rent rent = entityManager.find(Rent.class, id);
//            if (rent != null) {
//                rent.setEndTime(endDate);
//                entityManager.getTransaction().commit();
//                return rent;
//            }
//            entityManager.getTransaction().rollback();
//            return null;
//        }
//    }


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

//    @Override
    public List<Rent> getAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Rent> query = criteriaBuilder.createQuery(Rent.class);
            Root<Rent> root = query.from(Rent.class);
            query.select(root);


            return entityManager.createQuery(query).getResultList();
        }
    }
}
