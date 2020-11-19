package rnd.statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import rnd.statemachine.order.OrderEvent;

import java.util.UUID;

@Configuration
public class IntegrationConfig {

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(1000));
        return pollerMetadata;
    }

    @Autowired
    StateMachineFactory<String, String> factory;


    @Bean
    public MessageChannel createChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public MessageChannel orderIdChannel() {
        return MessageChannels.direct().get();
    }


    @Bean
    public MessageChannel postEventHandlerChannel() {
        return MessageChannels.queue(10).get();
    }

    @Bean
    public MessageChannel payChannel() {
        return MessageChannels.queue(10).get();
    }

    @Bean
    public MessageChannel syncResponseChannel() {
        return MessageChannels.queue(10).get();
    }

    @Bean
    public IntegrationFlow orderProcessorFlow() {
        return IntegrationFlows.from(createChannel())
                .log()
                .handle((payload, headers) -> {
                    UUID id = UUID.randomUUID();
                    StateMachine<String,String> fsm = factory.getStateMachine(id.toString());
                    fsm.sendEvent(OrderEvent.ORDERCREATED.name());
                    return id;
                })
                .channel(orderIdChannel())
                .get();
    }

    @Bean
    public IntegrationFlow paymentProcessorFlow() {
        return IntegrationFlows.from(payChannel())
                .log()
               .get();
    }

    @Bean
    public IntegrationFlow nameThisFlow() {
        return IntegrationFlows.from(postEventHandlerChannel())
                .log()
                .get();
    }
}
