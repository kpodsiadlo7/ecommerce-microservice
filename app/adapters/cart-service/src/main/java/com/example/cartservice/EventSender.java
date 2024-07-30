package com.example.cartservice;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventSender {
    private final static String UPDATE_PRODUCT = "product_update";

    void unReserveProducts(Long cartId, List<EventProductRecord> productsToUnReserve) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(UPDATE_PRODUCT, true, false, false, null);

            Gson gson = new Gson();
            String message = gson.toJson(EventRecord.builder().eventId(cartId).products(productsToUnReserve).build());
            channel.basicPublish("", UPDATE_PRODUCT, null, message.getBytes());
            log.info("[RABBIT-MQ] Products sent {}", productsToUnReserve);
        }
    }
}
