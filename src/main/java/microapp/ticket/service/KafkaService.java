package microapp.ticket.service;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class KafkaService {

    @Value(value = "${spring.cloud.stream.kafka.binder.brokers:localhost:9092}")
    private String kafkaAddress;

    @Autowired
    KafkaTemplate<String, String> template;

    Map<String, NewTopic> topics;

    public KafkaService() {
        topics = new HashMap<>();
    }

    public ListenableFuture<SendResult<String, String>> send(String topic, String message) {
        System.out.println("Sent Message in group: " + message);
        return template.send(topic, message);
    }

    public NewTopic getTopic(String topicName) {
        if (!topics.containsKey(topicName)) topics.put(topicName, new NewTopic(topicName, 1, (short) 1));
        return topics.get(topicName);
    }

    @KafkaListener(topics = "tag")
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group foo: " + message);
    }
}
