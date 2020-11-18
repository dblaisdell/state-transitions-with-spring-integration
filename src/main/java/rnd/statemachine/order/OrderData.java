package rnd.statemachine.order;

import java.util.UUID;

import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@Builder
@ToString
@Entity
public class OrderData {
	private double payment;
	private @Id UUID orderId;
	private String message;
}
