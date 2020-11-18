package rnd.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineModelConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.model.StateMachineModelFactory;
import org.springframework.statemachine.data.*;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.state.State;
import rnd.statemachine.order.OrderData;
import rnd.statemachine.order.OrderEvent;
import rnd.statemachine.order.OrderState;
import rnd.statemachine.repos.OrderDataRepository;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Configuration
public class StateMachineConfig {

    @Configuration
    public static class JpaPersisterConfig {

        @Bean
        public StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister(
                JpaStateMachineRepository jpaStateMachineRepository) {
            return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
        }
    }

    @Configuration
    @EnableStateMachineFactory
    public static class FactoryConfig extends StateMachineConfigurerAdapter<String, String> {

        private final Logger LOG =
                LoggerFactory.getLogger(StateMachineConfig.class);

        @Autowired
        OrderDataRepository orderDataRepository;

        @Autowired
        private StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister;

        @Override
        public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
            super.configure(config);
            config.withConfiguration()
                .autoStartup(true)
                    .listener(listener());

        config.withPersistence()
                .runtimePersister(stateMachineRuntimePersister);
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
            super.configure(transitions);
            transitions
                    .withExternal()
                    .source(OrderState.DEFAULT.name()).target(OrderState.PAYMENTPENDING.name()).event(OrderEvent.ORDERCREATED.name()).action(createOrderAction()).and()
                    .withExternal()
                    .source(OrderState.PAYMENTPENDING.name()).target(OrderState.PAYMENTPENDING.name()).event(OrderEvent.PAY.name()).action(payAction()).and()
                    .withExternal()
                    .source(OrderState.PAYMENTPENDING.name()).target(OrderState.PAYMENTSUCCESS.name()).event(OrderEvent.PAYMENTSUCCESS.name()).and()
                    .withExternal()
                    .source(OrderState.PAYMENTPENDING.name()).target(OrderState.PAYMENTERROR.name()).event(OrderEvent.PAYMENTERROR.name()).and()
                    .withExternal()
                    .source(OrderState.PAYMENTERROR.name()).target(OrderState.PAYMENTPENDING.name()).event(OrderEvent.RETRYPAY.name());

        }

        @Override
        public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
            super.configure(states);

            Set<String> stringStates = new HashSet<>();
            EnumSet.allOf(OrderState.class).forEach(entity -> stringStates.add(entity.name()));

            states.withStates()
                    .initial(OrderState.DEFAULT.name())
                    .end(OrderState.PAYMENTSUCCESS.name())
                    .states(stringStates);
        }

        @Bean
        public StateMachineListener<String, String> listener() {
            return new StateMachineListenerAdapter<String, String>() {

                @Override
                public void stateChanged(State<String, String> from, State<String, String> to) {
                    if (from == null) {
                        LOG.info("State changed from INITIAL to " + to.getId());
                    } else {
                        LOG.info("State changed from " + from.getId() + " to " + to.getId());
                    }
                }
            };
        }

        public Action<String, String> createOrderAction() {
            return stateContext -> {
                UUID id = UUID.randomUUID();
                OrderData data = new OrderData();
                data.setOrderId(id);
                data.setMessage("Order Created");
                data = orderDataRepository.save(data);
                stateContext.getExtendedState().getVariables().put("id", id);
                LOG.info("Created: " + data);
            };
        }

        public Action<String, String> payAction() {
            return stateContext -> {
                UUID id = (UUID) stateContext.getExtendedState().getVariables().get("id");
                OrderData data = orderDataRepository.findById(id).get();
                data.setMessage("PAYMENT IN PROGRESS");
                data = orderDataRepository.save(data);
                LOG.info("Pay Action: " + data);
            };
        }

    }
}