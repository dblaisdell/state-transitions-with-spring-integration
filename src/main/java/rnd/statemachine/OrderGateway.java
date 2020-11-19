package rnd.statemachine;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import rnd.statemachine.order.OrderData;

import java.util.UUID;

@MessagingGateway
public interface OrderGateway {

    @Gateway(requestChannel = "createChannel", replyChannel = "orderIdChannel")
    UUID createOrder(OrderData data);
}
