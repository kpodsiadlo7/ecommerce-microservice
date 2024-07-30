package com.example.cartservice;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Slf4j
@Entity
@Component
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private EventStatus eventStatus;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventProductEntity> products = new ArrayList<>();

    private final static String QUEUE_NAME = "product_update";

    void unReserveProducts(List<EventProductRecord> productsToUnReserve) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            Gson gson = new Gson();
            String message = gson.toJson(productsToUnReserve);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            log.info("Products sent \n {}", productsToUnReserve);
        }
    }
}
