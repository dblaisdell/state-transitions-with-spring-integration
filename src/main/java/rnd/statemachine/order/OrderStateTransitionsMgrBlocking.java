package rnd.statemachine.order;

import java.util.UUID;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import rnd.statemachine.AbstractStateTransitionsManager;
import rnd.statemachine.ProcessData;
import rnd.statemachine.ProcessException;

/**
 * This facade class manages the state transitions that require 
 * synchronous response
 */
@Slf4j
@Service
public class OrderStateTransitionsMgrBlocking extends AbstractStateTransitionsManager {

	@Autowired
	private final OrderDbService dbService;

	@Autowired
	private ApplicationContext applicationContext;

	public OrderStateTransitionsMgrBlocking(OrderDbService dbService) {
		this.dbService = dbService;
	}

	@Override
	protected ProcessData processStateTransition(ProcessData sdata) throws ProcessException {
 
		OrderData data = (OrderData) sdata;
		log.info("Initial state: " + (OrderState)((OrderEvent)data.getEvent()).nextState());
		log.info("Pre-event: " + data.getEvent().toString());
		Message<ProcessData> msg = MessageBuilder.withPayload((ProcessData)data).build();
		long timeout = data.getEvent().getTimeout();
		this.applicationContext.getBean(data.getEvent().getChannelName(), MessageChannel.class).send(msg, timeout);
		data = (OrderData)this.applicationContext.getBean("syncResponseChannel", PollableChannel.class).receive(timeout).getPayload();
		this.processPostEvent(data);
		return data;
	}

	private void processPostEvent(ProcessData data) {

		log.info("Post-event: " + data.getEvent().toString());

		this.dbService.saveState(((OrderData) data).getOrderId(), ((OrderState)((OrderEvent) data.getEvent()).nextState()).name());
				
		log.info("Final state: " + this.dbService.getOrderState(((OrderData) data).getOrderId()));
	}	

	@Override
	protected boolean hasValidPreState(ProcessData sdata) throws OrderException {

		OrderData data = (OrderData) sdata;
		
		OrderState currentState = (OrderState)((OrderEvent) data.getEvent()).nextState();
		
		if (data.getOrderId() == null) {
			UUID orderId = UUID.randomUUID();
			this.dbService.saveState(orderId, OrderState.DEFAULT.name());
			((OrderData) data).setOrderId(orderId);
			return true;
		} else {
			return currentState.name().equals(this.dbService.getOrderState(data.getOrderId())); 
		}
	}

}
