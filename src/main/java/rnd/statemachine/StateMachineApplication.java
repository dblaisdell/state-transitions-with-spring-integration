package rnd.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import rnd.statemachine.order.OrderEvent;

import java.util.UUID;

/**
 * A simple state machine implementation for Spring Boot projects Includes a
 * framework and a sample application of the framework to an online order
 * process. This version enables one or more processors to be non-blocking.
 * It uses Spring Integration framework to handle the non-blocking processes.
 *
 * @author Nalla Senthilnathan https://github.com/mapteb/state-transitions-with-spring-integration
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableIntegration
@EnableJpaRepositories(value = {"org.springframework.statemachine.data.jpa", "rnd.statemachine.repos"})
@EntityScan(value = {"org.springframework.statemachine.data.jpa", "rnd.statemachine.order"})
public class StateMachineApplication {

    @Component
    public class CommandLineAppStartupRunner implements ApplicationRunner {
        StateMachineFactory<String, String> factory;

        public CommandLineAppStartupRunner(StateMachineFactory<String, String> factory) {
            this.factory = factory;
        }

        private final Logger LOG =
                LoggerFactory.getLogger(CommandLineAppStartupRunner.class);


        @Override
        public void run(ApplicationArguments args) throws Exception {
            StateMachine<String, String> fsm1 = factory.getStateMachine("fsm1");

            StateMachine<String, String> fsm2 = factory.getStateMachine("fsm2");
            fsm1.sendEvent(MessageBuilder.withPayload("ORDERCREATED")
                    .setHeader("billAmount", 100.0)
                    .build());

//
            fsm2.sendEvent("ORDERCREATED");
            fsm2.sendEvent("PAY");
            fsm1.sendEvent("PAY");

//            fsm1.sendEvent(OrderEvent.PAY);
//
            fsm1.sendEvent(OrderEvent.PAYMENTERROR.name());
//
//            fsm1.sendEvent(MessageBuilder.withPayload(OrderEvent.RETRYPAY).build());
//
//            fsm1.sendEvent(OrderEvent.PAY);
//
//            fsm1.sendEvent(OrderEvent.PAYMENTSUCCESS);
        }
    }


    public static void main(String[] args) {
        SpringApplication.run(StateMachineApplication.class, args);
    }
}
