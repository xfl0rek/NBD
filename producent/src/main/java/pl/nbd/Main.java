package pl.nbd;

import pl.nbd.managers.RentManager;
import pl.nbd.model.*;
import pl.nbd.repository.RentRepository;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Address address = new Address("Laczna", "Lipinki", "43");
        Client client = new DefaultClient(1, "Syn", "Hymel", address);
        Room room = new RoomRegular(1, 100, 2, true);
        LocalDateTime startDate = LocalDateTime.now();
        RentRepository rentRepository = new RentRepository();
        RentManager rentManager = new RentManager(rentRepository);
        rentManager.rentRoom(1, client, room, startDate);
        KafkaProducent kafkaProducent = new KafkaProducent();
        Rent rent = rentManager.getRent(1);
        kafkaProducent.sendRent(rent);
        rentManager.returnRoom(1, LocalDateTime.now());
    }
}
