package com.example.cartservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventListener {
    private final static String UPDATE_STATUS = "product_status";

    private final EventService eventService;

    void listenerOnEvents() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(UPDATE_STATUS, true, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            log.info("[RABBIT-MQ] Received '{}'", message);

            processResponse(message);
        };
        channel.basicConsume(UPDATE_STATUS, true, deliverCallback, consumerTag -> {
        });
    }

    private void processResponse(String message) {
        try {
            Gson gson = new Gson();
            Type fileType = new TypeToken<EventReceiverRecord>() {
            }.getType();
            EventReceiverRecord event = gson.fromJson(message, fileType);

            eventService.updateCartStatus(event);
        } catch (
                Exception e) {
            log.error("Failed to parse JSON: {}", e.getMessage(), e);
        }
    }
}
