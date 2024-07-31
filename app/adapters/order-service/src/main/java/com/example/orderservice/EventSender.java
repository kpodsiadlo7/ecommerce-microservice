package com.example.orderservice;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
class EventSender {
    private final static String UPDATE_PRODUCT = "cart_status";

    void updateCartStatus(String cartId, OrderStatus status) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(UPDATE_PRODUCT, true, false, false, null);

            Gson gson = new Gson();
            String message = gson.toJson(EventRecord.builder().eventId(cartId).status(status).build());
            channel.basicPublish("", UPDATE_PRODUCT, null, message.getBytes());
            log.info("[RABBIT-MQ] Event sent {}", message);
        }
    }
}
