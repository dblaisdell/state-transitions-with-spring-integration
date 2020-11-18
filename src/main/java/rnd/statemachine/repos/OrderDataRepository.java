package rnd.statemachine.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnd.statemachine.order.OrderData;

import java.util.UUID;

@Repository
public interface OrderDataRepository extends JpaRepository<OrderData, UUID> {
}
