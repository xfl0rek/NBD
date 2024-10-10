package pl.nbd.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");


        Address address = new Address("Real", "Madryt", "7");
        PremiumType premiumType = new PremiumType();
        DefaultType defaultType = new DefaultType();
        Client client = new Client("Cristiano", "Ronaldo", "123456789", address, premiumType);
        Room room = new Room(1000, 7, 2);
        Rent rent = new Rent(UUID.randomUUID(), client, room, LocalDateTime.now());
        LocalDateTime endTime = LocalDateTime.now().plus(Duration.ofHours(168));
        rent.endRent(endTime);

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(client);
            em.getTransaction().commit();
        }

        System.out.println("Liczba dni wynajmu: " + rent.getRentDays());
        System.out.println("Ostateczny koszt wynajmu: " + rent.getRentCost());
    }
}
