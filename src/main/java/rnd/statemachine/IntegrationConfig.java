package rnd.statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.support.PeriodicTrigger;
import rnd.statemachine.ProcessData;
import rnd.statemachine.order.*;

@Configuration
public class IntegrationConfig {

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(1000));
        return pollerMetadata;
    }

    @Bean
    public MessageChannel createChannel() {
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
    public IntegrationFlow orderProcessorFlow(@Autowired OrderProcessor orderProcessor) {
        return IntegrationFlows.from(createChannel())
                .<ProcessData>handle((payload, headers) -> {
                    return orderProcessor.process(payload);
                }).get();
    }

    @Bean
    public IntegrationFlow paymentProcessorFlow(@Autowired PaymentProcessor paymentProcessor) {
        return IntegrationFlows.from(payChannel())
                .<ProcessData>handle((payload, headers) -> {
                    return paymentProcessor.process(payload);
                }).get();
    }

    @Bean
    public IntegrationFlow nameThisFlow(@Autowired OrderDbService orderDbService) {
        return IntegrationFlows.from(postEventHandlerChannel())
                .<ProcessData>handle((payload, headers) -> {
                    System.out.println("Post-event: " + payload.getEvent().toString());
                    orderDbService.saveState(((OrderData) payload).getOrderId(),
                            ((OrderState) payload.getEvent().nextState()).name());
                    System.out.println("Final state: " + orderDbService.getOrderState(((OrderData) payload).getOrderId()));
                    return payload;
                }).get();
    }
}
