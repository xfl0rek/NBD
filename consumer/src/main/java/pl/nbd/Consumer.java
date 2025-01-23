package pl.nbd;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Consumer {
    private final List<KafkaConsumer<Long, String>> kafkaConsumers = new ArrayList<>();
    private int numberOfConsumers;

    public Consumer(int numberOfConsumers) {
        this.numberOfConsumers = numberOfConsumers;
    }

    public List<KafkaConsumer<Long, String>> getKafkaConsumers() {
        return kafkaConsumers;
    }

    public void initConsumers() {
        Properties props = new Properties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group-rent");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
        if(kafkaConsumers.isEmpty()) {
            for (int i = 0; i < numberOfConsumers; i++) {
                KafkaConsumer<Long, String> kafkaConsumer = new KafkaConsumer<>(props);
                kafkaConsumer.subscribe(Collections.singleton("rents"));
                kafkaConsumers.add(kafkaConsumer);
            }
        }
    }

    public void consume(KafkaConsumer<Long, String> kafkaConsumer) {
        try {
            kafkaConsumer.poll(Duration.of(1000, ChronoUnit.MILLIS));
            Set<TopicPartition> consumerAssignment = kafkaConsumer.assignment();
            kafkaConsumer.seekToBeginning(consumerAssignment);
            Duration timeout = Duration.ofMillis(100);
            MessageFormat  messageFormat = new MessageFormat("ConsumerGroup {5}, Topic {0}, partition {1}, offset {2, number, integer}, key {3}, value {4}");
            while (true) {
                ConsumerRecords<Long, String> records = kafkaConsumer.poll(timeout);
                for (ConsumerRecord<Long, String> record : records) {
                    String result = messageFormat.format(new Object[]{
                            record.topic(), record.partition(), record.offset(), record.key(), record.value(),
                            kafkaConsumer.groupMetadata().memberId()
                    });

                    System.out.println(result);
                }
            }
        } catch (WakeupException wakeupException) {

        }
    }
}
