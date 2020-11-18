package rnd.statemachine.order;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class PaymentProcessor implements Action<OrderState,OrderEvent> {
    
       @Override
    public void execute(StateContext<OrderState, OrderEvent> stateContext) {
        try{
            //simulate a long running process
            Thread.sleep(1000L);

//            if(((OrderData)data).getPayment() > 0) {
//                ((OrderData)data).setEvent(OrderEvent.PAYMENTSUCCESS);
//                //TODO: send payment success email
//            } else {
//                ((OrderData)data).setEvent(OrderEvent.PAYMENTERROR);
//                //TODO: send payment error email
//            }
        }catch(InterruptedException e){
            //TODO: Use a new state transition to include system error
        }
    }
}
