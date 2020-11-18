package rnd.statemachine;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import rnd.statemachine.order.OrderEvent;
import rnd.statemachine.order.OrderState;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class ProcessStateMachine extends EnumStateMachineConfigurerAdapter<OrderState, OrderEvent> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderState, OrderEvent> config) throws Exception {
        super.configure(config);
        config.withConfiguration()
                .autoStartup(true);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) throws Exception {
        super.configure(transitions);
        transitions
                .withExternal()
                .source(OrderState.DEFAULT).target(OrderState.PAYMENTPENDING).event(OrderEvent.ORDERCREATED).and()
                .withExternal()
                .source(OrderState.PAYMENTPENDING).target(OrderState.PAYMENTPENDING).event(OrderEvent.PAY).and()
                .withExternal()
                .source(OrderState.PAYMENTPENDING).target(OrderState.PAYMENTSUCCESS).event(OrderEvent.PAYMENTSUCCESS).and()
                .withExternal()
                .source(OrderState.PAYMENTPENDING).target(OrderState.PAYMENTERROR).event(OrderEvent.PAYMENTERROR).and()
                .withExternal()
                .source(OrderState.PAYMENTERROR).target(OrderState.PAYMENTPENDING).event(OrderEvent.RETRYPAY);

    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception {
        super.configure(states);
        states.withStates()
                .initial(OrderState.DEFAULT)
                .end(OrderState.PAYMENTSUCCESS)
                .states(EnumSet.allOf(OrderState.class));
    }
}
