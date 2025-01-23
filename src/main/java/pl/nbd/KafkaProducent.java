package pl.nbd;

import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.codehaus.jackson.annotate.JsonBackReference;
import pl.nbd.model.Rent;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;


public class KafkaProducent {

    static KafkaProducer<Long, String> kafkaProducer;
    private final String RENT_TOPIC = "rents";
    private final Jsonb jsonb = JsonbBuilder.create();


    public KafkaProducent() throws ExecutionException, InterruptedException {
        initProducer();
    }
    public static void initProducer() {
        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, "local");
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "kafka1:9192,kafka2:9292,kafka3:9392");
        producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
        producerConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        kafkaProducer = new KafkaProducer<>(producerConfig);
    }


    public void sendRent(Rent rent) throws InterruptedException {
        //createTopic();
        Jsonb jsonb = JsonbBuilder.create();
        String rentJSON = jsonb.toJson(rent);

        ProducerRecord<Long, String> record = new ProducerRecord<>(RENT_TOPIC, rent.getId(), rentJSON);

        System.out.println("Sending rent: " + rentJSON);
    kafkaProducer.send(record, this::onCompletion);
        System.out.println("Sent rent: " + rentJSON);
    }

    private void onCompletion(RecordMetadata metadata, Exception exception) {
        System.out.println("Record sent");
        if (exception == null) {
            System.out.println("Record sent with key " + metadata.offset());
            System.out.println(metadata.offset());
        } else {
            System.out.println(exception);
        }
    }

    public void createTopic() throws InterruptedException {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
        int partitionsNumber = 3;
        short replicationFactor = 3;
        try (Admin admin = Admin.create(properties)) {
            NewTopic newTopic = new NewTopic(RENT_TOPIC, partitionsNumber, replicationFactor);
            CreateTopicsOptions options = new CreateTopicsOptions()
                    .timeoutMs(1000)
                    .validateOnly(false)
                    .retryOnQuotaViolation(true);
            CreateTopicsResult result = admin.createTopics(List.of(newTopic), options);
            KafkaFuture<Void> futureResult = result.values().get(RENT_TOPIC);
            futureResult.get();
        } catch (ExecutionException ee) {
            System.out.println(ee.getCause());
        }
    }
}
