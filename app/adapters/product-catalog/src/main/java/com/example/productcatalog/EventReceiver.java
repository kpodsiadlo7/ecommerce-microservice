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
import java.util.List;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
public class EventReceiver {
    private final static String QUEUE_NAME = "product_update";
    private static final Logger log = LoggerFactory.getLogger(EventReceiver.class);

    private final EventService eventService;

    void listenerOnEvents() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            log.info("[RABBIT-MQ] Received '{}'", message);
            processMessage(message);
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }

    private void processMessage(String message) {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<EventProductRecord>>() {
            }.getType();
            List<EventProductRecord> products = gson.fromJson(message, listType);

            eventService.unReserveProducts(products);
        } catch (Exception e) {
            log.error("Failed to parse JSON: {}", e.getMessage(), e);
        }
    }
}
