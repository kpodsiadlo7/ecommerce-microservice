package com.example.productcatalog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
public class EventManager {
    private final static String PRODUCT_RECEIVE = "product_update";
    private final static String PRODUCT_STATUS = "product_status";
    private static final Logger log = LoggerFactory.getLogger(EventManager.class);

    private final EventService eventService;

    void listenerOnEvents() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(PRODUCT_RECEIVE, true, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            log.info("[RABBIT-MQ] Received '{}'", message);

            processResponse(message);
        };
        channel.basicConsume(PRODUCT_RECEIVE, true, deliverCallback, consumerTag -> {
        });
    }

    private void processResponse(String message) {
        try {
            Gson gson = new Gson();
            Type fileType = new TypeToken<EventReceiverRecord>() {
            }.getType();
            EventReceiverRecord event = gson.fromJson(message, fileType);

            if (eventService.unReserveProducts(event)) {
                sendStatus(event.eventId(), EventStatusRecord.Status.COMPLETED);
            } else {
                sendStatus(event.eventId(), EventStatusRecord.Status.FAILED);
            }
        } catch (
                Exception e) {
            log.error("Failed to parse JSON: {}", e.getMessage(), e);
        }
    }

    private void sendStatus(Long eventId, EventStatusRecord.Status eventStatus) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(PRODUCT_STATUS, true, false, false, null);

            Gson gson = new Gson();
            String message = gson.toJson(EventStatusRecord.builder().eventId(eventId).status(eventStatus).build());
            channel.basicPublish("", PRODUCT_STATUS, null, message.getBytes(StandardCharsets.UTF_8));
            log.info("[RABBIT-MQ] Sent status '{}'", message);

        } catch (IOException | TimeoutException e) {
            log.error("Failed to send status: {}", e.getMessage(), e);
        }
    }
}
