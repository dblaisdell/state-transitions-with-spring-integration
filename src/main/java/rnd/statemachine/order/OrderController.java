package rnd.statemachine.order;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import rnd.statemachine.ProcessException;

@RequiredArgsConstructor
@RestController
public class OrderController {

    @Autowired
    private final OrderStateTransitionsMgrBlocking stateTrasitionsMgrBlocking;

    @Autowired
    private final OrderStateTransitionsMgrNonBlocking stateTrasitionsMgrNonBlocking;

    @Autowired
    private OrderDbService orderDbService;
    /**
     * Quick API to test the payment event
     * @param amount
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/orders/{id}/payment/{amount}")
    public String handleOrderPayment( 
            @PathVariable double amount,
            @PathVariable UUID id) throws Exception {

        OrderData data = new OrderData();
    	data.setPayment(amount);
    	data.setOrderId(id);
    	data.setEvent(OrderEvent.PAY);
    	data = (OrderData)stateTrasitionsMgrNonBlocking.processPreEvent(data);
    	
        return ((OrderEvent)data.getEvent()).getMessage();
    }
    
    @PostMapping("/orders/{id}/retrypayment/{amount}")
    public String handleOrderRePayment( 
            @PathVariable double amount,
            @PathVariable UUID id) throws Exception {

        OrderData data = new OrderData();
    	data.setPayment(amount);
    	data.setOrderId(id);
    	data.setEvent(OrderEvent.RETRYPAY);
    	data = (OrderData)stateTrasitionsMgrNonBlocking.processPreEvent(data);
    	
        return ((OrderEvent)data.getEvent()).getMessage();
    }
    
    @ExceptionHandler(value=ProcessException.class)
    public String handleOrderException(ProcessException e) {
        return e.getMessage();
    }
    
    @ExceptionHandler(value=IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException e) {
        return e.getMessage();
    }
    
    /**
     * API to test the order create event
     * @return
     * @throws ProcessException
     */
    @PostMapping("/order/items")
    public String handleOrderCreate() throws ProcessException {

        OrderData data = new OrderData();
        data.setEvent(OrderEvent.CREATE);
        data = (OrderData)stateTrasitionsMgrBlocking.processPreEvent(data);
        return ((OrderData)data).getMessage();
    }

    @ResponseBody
    @GetMapping("/orders")
    public List<OrderData> getOrders() {
        return orderDbService.getOrders();
    }
}

