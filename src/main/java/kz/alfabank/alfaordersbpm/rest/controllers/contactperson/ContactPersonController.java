package kz.alfabank.alfaordersbpm.rest.controllers.contactperson;

import io.swagger.annotations.*;
import kz.alfabank.alfaordersbpm.domain.models.audit.Auditable;
import kz.alfabank.alfaordersbpm.domain.models.contactperson.ContactPerson;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.rest.controllers.Constants;
import kz.alfabank.alfaordersbpm.domain.service.contactperson.ContactPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "API контактных лиц")
@CrossOrigin
@RestController
@RequestMapping(Constants.API_BASE + "/retail/v1/contactpersons")
public class ContactPersonController {

    private static final Logger LOG = LoggerFactory.getLogger(ContactPersonController.class);

    private final ContactPersonService contactPersonService;

    @Autowired
    public ContactPersonController(ContactPersonService contactPersonService) {
        this.contactPersonService = contactPersonService;
    }

    @ApiOperation(value = "Контактные лица заявки", notes = "Контактные лица заявки")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/orders/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Полуение контактных лиц заявки")
    @PreAuthorize("hasPermission(#orderId, 'OrderContactPerson', 'read')")
    public ResponseEntity<List<ContactPerson>> getOrderContactPersons(@ApiParam(value = "ИД заявки", required = true) @PathVariable Long orderId) {
        LOG.debug("REST request to getOrderContactPersons OrderId={}", orderId);
        if (orderId == null)
            throw new BadRequestException("To getOrderContactPersons orderId must not be null");
        List<ContactPerson> result = contactPersonService.getByOrderId(orderId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Получить контактное лицо по ИД", notes = "Получить адрес по ИД")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Полуение контактноного лица по ИД")
    @PreAuthorize("hasPermission(#id, 'ContactPerson', 'read')")
    public ResponseEntity<ContactPerson> getContactPersonById(@ApiParam(value = "ИД записи", required = true) @PathVariable Long id) {
        LOG.debug("REST request to getContactPersonById id={}", id);
        ContactPerson result = contactPersonService.getOne(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("ContactPerson not found with ID = %s",id.toString())));
        return ResponseEntity.ok(result);
    }
}
