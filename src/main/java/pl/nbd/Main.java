package pl.nbd;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Address address = new Address("Real", "Madryt", "7");
        PremiumType premium = new PremiumType();
        DefaultType defaults = new DefaultType();
        Client client = new Client("Cristiano", "Ronaldo", "123456789", address, premium);
        Room room = new Room(1000, 7, 2);
        Rent rent = new Rent(UUID.randomUUID(), client, room, LocalDateTime.now());
        LocalDateTime endTime = LocalDateTime.now().plus(Duration.ofHours(168));
        rent.endRent(endTime);

        System.out.println("Liczba dni wynajmu: " + rent.getRentDays());
        System.out.println("Ostateczny koszt wynajmu: " + rent.getRentCost());
    }
}
