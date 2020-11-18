package rnd.statemachine.order;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessor implements Action<OrderState,OrderEvent> {

//    @Override
//    public ProcessData process(ProcessData data) {
//
//        ((OrderData) data).setMessage("Order created, Taxes 12.00, Shipping 123.00, Total 1234.00, orderId = " + ((OrderData) data).getOrderId());
//        ((OrderData) data).setEvent(OrderEvent.ORDERCREATED);
//        return data;
//    }

    @Override
    public void execute(StateContext<OrderState, OrderEvent> stateContext) {
//        ((OrderData) data).setMessage("Order created, Taxes 12.00, Shipping 123.00, Total 1234.00, orderId = " + ((OrderData) data).getOrderId());
//        ((OrderData) data).setEvent(OrderEvent.ORDERCREATED);
//        return data;
    }
}
