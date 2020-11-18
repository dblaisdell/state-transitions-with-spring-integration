package rnd.statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.statemachine.config.EnableWithStateMachine;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * A simple state machine implementation for Spring Boot projects Includes a
 * framework and a sample application of the framework to an online order
 * process. This version enables one or more processors to be non-blocking.
 * It uses Spring Integration framework to handle the non-blocking processes.
 * 
 * @author Nalla Senthilnathan https://github.com/mapteb/state-transitions-with-spring-integration
 *
 */
@EnableIntegration
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = {"rnd.statemachine.order"})
@EnableWithStateMachine
public class StateMachineApplication {

	@Autowired
	private ProcessStateMachine stateMachine;

	public static void main(String[] args) {
		SpringApplication.run(StateMachineApplication.class, args);
	}
}
