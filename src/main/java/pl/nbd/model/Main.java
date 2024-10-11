package pl.nbd.model;

import pl.nbd.repository.ClientRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        ClientRepository clientRepository  = new ClientRepository();

        Address address = new Address("Real", "Madryt", "9");
        Random random = new Random();
        Client client = new PremiumClient(100_000_000L + random.nextInt(900_000_000), "Cristiano", "Ronaldo", address);
        Client client1 = new DefaultClient(100_000_000L + random.nextInt(900_000_000), "Leo", "Messi", address);

        clientRepository.create(client);
        clientRepository.create(client1);

        Client read = clientRepository.read(client.getPersonalID());
        System.out.println("Odczytano klienta: " + read.getFirstName() + " " + read.getLastName());

        client.setFirstName("Vinicius");
        client.setLastName("Junior");
        clientRepository.update(client);
        Client read2 = clientRepository.read(client.getPersonalID());
        System.out.println("Odczytano klienta: " + read2.getFirstName() + " " + read2.getLastName());

        clientRepository.delete(client);
        Client read3 = clientRepository.read(client.getPersonalID());
        if (read3 == null) {
            System.out.println("Klient został usunięty.");
        }

        Room room = new RoomRegular(1000, 9, 2, true);
        Room room2 = new RoomChildren(1000, 10, 2, 3);
        Rent rent = new Rent(random.nextLong(), client, room, LocalDateTime.now());
        Rent rent1 = new Rent(random.nextLong(), client1, room2, LocalDateTime.now());
        LocalDateTime endTime = LocalDateTime.now().plus(Duration.ofHours(168));
        rent.endRent(endTime);
        rent1.endRent(endTime);

        System.out.println("Liczba dni wynajmu: " + rent.getRentDays());
        System.out.println("Ostateczny koszt wynajmu: " + rent.getRentCost());
        System.out.println("Ostateczny koszt wynajmu: " + rent1.getRentCost());
        System.out.println(room.getRoomCapacity());
    }
}
