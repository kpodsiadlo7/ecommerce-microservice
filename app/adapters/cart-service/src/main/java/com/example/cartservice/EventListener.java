package com.example.cartservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
class EventListener {
    private final static String PAYMENT_STATUS = "payment_status";
    private final static String PRODUCT_STATUS = "product_status";

    private final EventService eventService;

    void listenerOnEvents() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(PAYMENT_STATUS, true, false, false, null);
        channel.queueDeclare(PRODUCT_STATUS, true, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            log.info("[RABBIT-MQ] Received '{}'", message);

            try {
                processResponse(message, consumerTag);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
        };

        channel.basicConsume(PAYMENT_STATUS, true, "payment_status", deliverCallback, consumerTag -> {
        });
        channel.basicConsume(PRODUCT_STATUS, true, "product_status", deliverCallback, consumerTag -> {
        });
    }

    private void processResponse(String message, String consumerTag) throws IOException, TimeoutException {
        Gson gson = new GsonBuilder().create();
        EventReceiverRecord event;
        try {
            event = gson.fromJson(message, EventReceiverRecord.class);
            if (event == null || event.status() == null || event.eventId() == null) {
                throw new IllegalArgumentException("Invalid event receiver record!");
            }
            log.info("Successfully parsed event: {}", event);
            if ("payment_status".equals(consumerTag)) {
                log.info("Processing payment status");
                eventService.updateCartStatus(event);
            } else if ("product_status".equals(consumerTag)) {
                log.info("Processing product status");
                eventService.updateEventStatus(event);
            }
        } catch (Exception e) {
            log.error("Failed to parse JSON: {}", message, e);
        }
    }
}
