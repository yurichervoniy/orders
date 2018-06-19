package kz.alfabank.alfaordersbpm.rest.controllers.phone;

import io.swagger.annotations.*;
import kz.alfabank.alfaordersbpm.domain.models.audit.Auditable;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.phone.Phone;
import kz.alfabank.alfaordersbpm.domain.models.phone.PhoneType;
import kz.alfabank.alfaordersbpm.rest.controllers.Constants;
import kz.alfabank.alfaordersbpm.domain.service.phone.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "API телефонов по заявкам")
@CrossOrigin
@RestController
@RequestMapping(Constants.API_BASE + "/retail/v1/phones")
public class PhoneController {
    private static final Logger LOG = LoggerFactory.getLogger(PhoneController.class);

    private final PhoneService phoneService;

    @Autowired
    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @ApiOperation(value = "Телефоны заявки", notes = "Телефоны заявки")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Телефоны заявки")
    @PreAuthorize("hasPermission(#orderId, 'OrderPhone', 'read')")
    public ResponseEntity<List<Phone>> getOrderPhones(@ApiParam(value = "ИД заявки", required = true) @RequestParam("orderId") Long orderId) {
        LOG.debug("REST request to get getOrderPhones OrderId={}", orderId);
        if (orderId == null)
            throw new BadRequestException("To getOrderPhones orderId must not be null");

        return ResponseEntity.ok(phoneService.getByOrderId(orderId));
    }

    @ApiOperation(value = "Телефоны заявки по типу", notes = "Телефоны заявки по типу")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/orders/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Телефоны заявки по типу")
    @PreAuthorize("hasPermission(#orderId, 'OrderPhone', 'read')")
    public ResponseEntity<List<Phone>> getOrderPhonesByType(@ApiParam(value = "ИД заявки", required = true) @PathVariable Long orderId,
                                                         @ApiParam(value = "Тип телефона", required = true, allowableValues = "MOBILE, HOME, WORK")
                                                         @RequestParam(value = "phoneType") PhoneType phoneType) {
        LOG.debug("REST request to get phones OrderId={} PhoneType={}", orderId, phoneType);
        if (orderId == null)
            throw new BadRequestException("To getOrderPhonesByType orderId must not be null");

        List<Phone> phones = phoneService.getByOrdeIdAndPhoneType(orderId, phoneType);
        return ResponseEntity.ok(phones);
    }

    @ApiOperation(value = "Получить телефон по ИД", notes = "Получить телефон по ИД")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Получить телефон по ИД")
    @PreAuthorize("hasPermission(#id, 'Phone', 'read')")
    public ResponseEntity<Phone> getPhoneById(@ApiParam(value = "ИД записи", required = true) @PathVariable Long id) {
        LOG.debug("REST request to getPhoneById id={}", id);
        Phone result = phoneService.getOne(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Phone not found with ID = %s",id.toString())));
        return ResponseEntity.ok(result);
    }


    @ApiOperation(value = "Обновить телефон", notes = "Обновить телефон")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Обновить телефон")
    @PreAuthorize("hasPermission(#phone.orderId, 'OrderPhone', 'write')")
    public ResponseEntity<Phone> updatePhone(@ApiParam(value = "документ", required = true) @Valid @RequestBody Phone phone) {
        LOG.debug("REST request to updatePhone {}", phone);
        Phone result = phoneService.update(phone);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Удалить телефон", notes = "Удалить телефон")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Удалить телефон")
    @PreAuthorize("hasPermission(#id, 'Phone', 'write')")
    public ResponseEntity<Void> deletePhoneById(@ApiParam(value = "ИД записи", required = true) @PathVariable Long id) {
        LOG.debug("REST request to deletePhoneById id={}", id);
        phoneService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
