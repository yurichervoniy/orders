package kz.alfabank.alfaordersbpm.domain.service.movement;

import kz.alfabank.alfaordersbpm.domain.models.movement.OrderMovement;

import java.sql.SQLException;
import java.util.List;

public interface OrderMovementService {

    List<OrderMovement> getByOrderId(Long orderId) throws SQLException;

}
