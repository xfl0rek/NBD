package pl.nbd.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");


        Address address = new Address("Real", "Madryt", "9");
        PremiumType premiumType = new PremiumType();
        DefaultType defaultType = new DefaultType();
        Random random = new Random();
        Client client = new DefaultType(random.nextLong(), "Cristiano", "Ronaldo", address);
        Room room = new RoomRegular(1000, 9, 2, true);
        Room room2 = new RoomChildren(1000, 10, 2, 3);
        Rent rent = new Rent(random.nextLong(), client, room, LocalDateTime.now());
        LocalDateTime endTime = LocalDateTime.now().plus(Duration.ofHours(168));
        rent.endRent(endTime);


        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(client);
            em.persist(room);
            em.persist(room2);
            em.persist(rent);
            em.getTransaction().commit();
        }



        System.out.println("Liczba dni wynajmu: " + rent.getRentDays());
        System.out.println("Ostateczny koszt wynajmu: " + rent.getRentCost());
        System.out.println(room.getRoomCapacity());
    }
}
