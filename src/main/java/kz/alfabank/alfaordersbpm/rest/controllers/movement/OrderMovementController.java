package kz.alfabank.alfaordersbpm.rest.controllers.movement;

import io.swagger.annotations.*;
import kz.alfabank.alfaordersbpm.domain.models.audit.Auditable;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.movement.OrderMovement;
import kz.alfabank.alfaordersbpm.domain.service.movement.OrderMovementService;
import kz.alfabank.alfaordersbpm.rest.controllers.Constants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;


@Api(value = "API движения заявок")
@CrossOrigin
@RestController
@RequestMapping(Constants.API_BASE + "/retail/v1/ordermovement")
@Validated
public class OrderMovementController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderMovementController.class);

    private final OrderMovementService orderMovementService;

    @Autowired
    public OrderMovementController(OrderMovementService orderMovementService) {
        this.orderMovementService = orderMovementService;
    }

    @ApiOperation(value = "Движение заявки", notes = "Движение заявки")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Получение движения заявки")
    @PreAuthorize("hasPermission(#orderId, 'OrderMovement', 'read')")
    public List<OrderMovement> getOrderMovement(@ApiParam(value = "ИД заявки") @PathVariable Long orderId) throws SQLException{
        LOG.debug("REST request to get OrderMovement OrderId={}", orderId);
        if (orderId == null)
            throw new BadRequestException("To getOrderMovement orderId must not be null");
        return orderMovementService.getByOrderId(orderId);
    }
}
