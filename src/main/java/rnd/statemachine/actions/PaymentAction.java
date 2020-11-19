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
public class PaymentAction implements Action<String, String> {

    @Autowired
    private OrderDataRepository orderDataRepository;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(StateContext<String, String> stateContext) {
        UUID id = (UUID) stateContext.getExtendedState().getVariables().get("id");
        OrderData data = orderDataRepository.findById(id).get();
        data.setMessage("PAYMENT IN PROGRESS");
        data = orderDataRepository.save(data);
        LOG.info("Pay Action: " + data);
    }
}
