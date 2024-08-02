package com.example.paymentservice;

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
    private final static String UPDATE_PAYMENT = "payment_status";

    void updatePaymentStatus(String cartId, PaymentStatus status) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(UPDATE_PAYMENT, true, false, false, null);

            Gson gson = new Gson();
            String message = gson.toJson(EventRecord.builder().eventId(cartId).status(status).build());
            channel.basicPublish("", UPDATE_PAYMENT, null, message.getBytes());
            log.info("[RABBIT-MQ] Event sent {}", message);
        }
    }
}
