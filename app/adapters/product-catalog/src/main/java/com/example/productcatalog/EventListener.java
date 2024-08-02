package com.example.productcatalog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventListener {
    private final static String PRODUCT_RECEIVE = "product_update";
    private final static String PRODUCT_STATUS = "product_status";

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
        Gson gson = new GsonBuilder().create();
        EventReceiverRecord event = null;
        try {
            event = gson.fromJson(message, EventReceiverRecord.class);
            log.info("Successfully parsed event: {}", event);
        } catch (Exception e) {
            log.error("Failed to parse JSON: {}", message, e);
        }
        if (event == null) throw new RuntimeException("Event is null!");
        if (eventService.unreserveProducts(event)) {
            sendStatus(event.eventId(), EventStatusRecord.Status.COMPLETE);
        } else {
            sendStatus(event.eventId(), EventStatusRecord.Status.FAILED);
        }
    }

    private void sendStatus(String eventId, EventStatusRecord.Status eventStatus) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(PRODUCT_STATUS, true, false, false, null);

            Gson gson = new Gson();
            String message = gson.toJson(EventStatusRecord.builder().eventId(String.valueOf(eventId)).status(eventStatus).build());
            channel.basicPublish("", PRODUCT_STATUS, null, message.getBytes(StandardCharsets.UTF_8));
            log.info("[RABBIT-MQ] Sent status '{}'", message);

        } catch (IOException | TimeoutException e) {
            log.error("Failed to send status: {}", e.getMessage(), e);
        }
    }
}
