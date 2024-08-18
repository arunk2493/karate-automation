package com.example.utils;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KafkaUtils {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final int REPLICATION_FACTOR = 5;  // Set your desired replication factor

    public static void createTopicIfNotExists(String topic) {
        Properties adminProps = new Properties();
        adminProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        try (AdminClient adminClient = AdminClient.create(adminProps)) {
            NewTopic newTopic = new NewTopic(topic, 5, (short) REPLICATION_FACTOR);
            CreateTopicsResult result = adminClient.createTopics(Collections.singletonList(newTopic));
            try {
                result.all().get(); // Wait for topic creation to complete
            } catch (InterruptedException | ExecutionException e) {
                if (e.getCause() instanceof TopicExistsException) {
                    System.out.println("Topic already exists: " + topic);
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

   

    public static void produceToKafka(String topic, String key, Map<String, Object> jsonMap) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonMessage = mapper.writeValueAsString(jsonMap);

        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps)) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, jsonMessage);
            producer.send(record);
        }
    }



    public static String consumeFromKafka(String topic, String groupId) {
        createTopicIfNotExists(topic);  // Ensure the topic exists with the desired replication factor

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        try (Consumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(topic));

            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                return record.value();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
