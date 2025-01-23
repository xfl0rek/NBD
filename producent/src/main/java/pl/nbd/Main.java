package pl.nbd;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public class Main {
    public static void main(String[] args) {
        Consumer consumer = new Consumer(3);
        consumer.initConsumers();

        for (KafkaConsumer<Long, String> kafkaConsumer : consumer.getKafkaConsumers()) {
            new Thread(() -> consumer.consume(kafkaConsumer)).start();
        }
    }
}
