package kz.alfabank.alfaordersbpm.rest.controllers.address;

import io.swagger.annotations.*;
import kz.alfabank.alfaordersbpm.domain.models.address.Address;
import kz.alfabank.alfaordersbpm.domain.models.address.AddressReadPermission;
import kz.alfabank.alfaordersbpm.domain.models.address.AddressType;
import kz.alfabank.alfaordersbpm.domain.models.audit.Auditable;
import kz.alfabank.alfaordersbpm.domain.models.dto.AddressDTO;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.rest.controllers.Constants;
import kz.alfabank.alfaordersbpm.domain.service.address.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.util.List;

@Api(value = "API адресов заявок")
@CrossOrigin
@RestController
@RequestMapping(Constants.API_BASE + "/retail/v1/addresses")
public class AddressController {

    private static final Logger LOG = LoggerFactory.getLogger(AddressController.class);

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @ApiOperation(value = "Адреса заявки", notes = "Адреса заявки")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Получение адресов заявки")
    @AddressReadPermission
    public ResponseEntity<List<Address>> getOrderAddresses(@ApiParam(value = "ИД заявки", required = true) @RequestParam("orderId") Long orderId) {
        LOG.debug("REST request to get address OrderId={}", orderId);
        if (orderId == null)
            throw new BadRequestException("To getOrderAddresses orderId must not be null");

        return ResponseEntity.ok(addressService.getByOrderId(orderId));
    }

    @ApiOperation(value = "Адрес заявки по типу", notes = "Адрес заявки по типу")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/orders/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Получение адресов заявки по типу")
    @AddressReadPermission
    public ResponseEntity<Address> getOrderAddressByType(@ApiParam(value = "ИД заявки", required = true) @PathVariable Long orderId,
                                                         @ApiParam(value = "Тип адреса", required = true, allowableValues = "REGISTRATION, WORK, RESIDENCE")
                                                         @RequestParam(value = "addressType") AddressType addressType) {
        LOG.debug("REST request to get address OrderId={} AddressType={}", orderId, addressType);
        if (orderId == null)
            throw new BadRequestException("To getAddresses orderId must not be null");
        Address address = addressService.getByOrdeIdAndAddresType(orderId, addressType)
                                        .orElseThrow(()-> new EntityNotFoundException(String.format("Address with orderId=[%s] and addressType=[%s] not found", orderId, addressType)));
        return ResponseEntity.ok(address);
    }

    @ApiOperation(value = "Получить адрес по ИД", notes = "Получить адрес по ИД")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Получение адреса заявки по ИД")
    @PreAuthorize("hasPermission(#id, 'Address', 'read')")
    public ResponseEntity<Address> getAddressById(@ApiParam(value = "ИД записи", required = true) @PathVariable Long id) {
        LOG.debug("REST request getAddressById by id={}", id);
        Address result = addressService.getOne(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Entity not found with ID = %s",id.toString())));
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Добавление адреса для заявки", notes = "Добавление адреса для заявки")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Добавление адреса для заявки")
    @PreAuthorize("hasPermission(#addressDTO.orderId, 'OrderAddress', 'write')")
    public ResponseEntity<Address> createAddress(@ApiParam(value = "address", required = true) @Valid @RequestBody AddressDTO addressDTO) {
        LOG.debug("REST request to createAddress {}", addressDTO);
        Address result = addressService.insertFromAddressDTO(addressDTO);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri())
                .body(result);
    }

    @ApiOperation(value = "Обновить адрес", notes = "Обновить адрес")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Обновление адреса для заявки")
    @PreAuthorize("hasPermission(#address.orderId, 'OrderAddress', 'write')")
    public ResponseEntity<Address> updateAddress(@ApiParam(value = "документ", required = true) @Valid @RequestBody Address address) {
        LOG.debug("REST request to updateAddress {}", address);
        Address result = addressService.update(address);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Удалить адрес", notes = "Удалить адрес")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Удаление адреса для заявки по ИД")
    @PreAuthorize("hasPermission(#id, 'Address', 'write')")
    public ResponseEntity<Void> deleteAddressById(@ApiParam(value = "ИД записи", required = true) @PathVariable Long id) {
        LOG.debug("REST request to deleteAddressById {}", id);
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
