package pl.nbd.managers;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import pl.nbd.Consumer;
import pl.nbd.KafkaProducent;
import pl.nbd.model.*;

import java.time.LocalDateTime;

public class test {

    @Test
    void test() throws Exception {
        Address address = new Address("Laczna", "Lipinki", "43");
        Client client = new DefaultClient(1, "Jadwiga", "Hymel", address);
        Room room = new RoomRegular(1, 100, 2, true);
        LocalDateTime startDate = LocalDateTime.now();
        Rent rent = new Rent(1, client, room, startDate);
        KafkaProducent kafkaProducent = new KafkaProducent();
        kafkaProducent.sendRent(rent);
        Consumer consumer = new Consumer(3);
 //       consumer.initConsumers();

//        for (KafkaConsumer<Long, String> kafkaConsumer : consumer.getKafkaConsumers()) {
//            new Thread(() -> consumer.consume(kafkaConsumer)).start();
//        }
    }
}
