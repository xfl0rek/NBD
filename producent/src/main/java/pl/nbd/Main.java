package pl.nbd;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import pl.nbd.model.*;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Address address = new Address("Laczna", "Lipinki", "43");
        Client client = new DefaultClient(1, "Syn", "Hymel", address);
        Room room = new RoomRegular(1, 100, 2, true);
        LocalDateTime startDate = LocalDateTime.now();
        Rent rent = new Rent(1, client, room, startDate);
        KafkaProducent kafkaProducent = new KafkaProducent();
        while (true) {
            kafkaProducent.sendRent(rent);
            Thread.sleep(10000);
        }
    }
}
