package pl.nbd.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");


        Address address = new Address("Real", "Madryt", "9");
        Random random = new Random();
        Client client = new PremiumClient(random.nextLong(), "Cristiano", "Ronaldo", address);
        Client client1 = new DefaultClient(random.nextLong(), "Leo", "Messi", address);
        Room room = new RoomRegular(1000, 9, 2, true);
        Room room2 = new RoomChildren(1000, 10, 2, 3);
        Rent rent = new Rent(random.nextLong(), client, room, LocalDateTime.now());
        Rent rent1 = new Rent(random.nextLong(), client1, room2, LocalDateTime.now());
        LocalDateTime endTime = LocalDateTime.now().plus(Duration.ofHours(168));
        rent.endRent(endTime);
        rent1.endRent(endTime);


        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(client);
            em.persist(client1);
            em.persist(room);
            em.persist(room2);
            em.persist(rent);
            em.persist(rent1);
            em.getTransaction().commit();
        }

        System.out.println("Liczba dni wynajmu: " + rent.getRentDays());
        System.out.println("Ostateczny koszt wynajmu: " + rent.getRentCost());
        System.out.println("Ostateczny koszt wynajmu: " + rent1.getRentCost());
        System.out.println(room.getRoomCapacity());
    }
}
