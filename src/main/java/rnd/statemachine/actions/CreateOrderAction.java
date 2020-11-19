package rnd.statemachine.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import rnd.statemachine.order.OrderData;
import rnd.statemachine.repos.OrderDataRepository;

import java.util.UUID;

@Component
public class CreateOrderAction implements Action<String, String> {

    @Autowired
    private OrderDataRepository orderDataRepository;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(StateContext<String, String> stateContext) {
        UUID id = UUID.randomUUID();
        OrderData data = new OrderData();
        data.setOrderId(id);
        data.setMessage("Order Created");
        data = orderDataRepository.save(data);
        stateContext.getExtendedState().getVariables().put("id", id);
        LOG.info("Created: " + data);
    }
}

