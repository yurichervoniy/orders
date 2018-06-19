package kz.alfabank.alfaordersbpm.rest.controllers.identitydocument;

import io.swagger.annotations.*;
import kz.alfabank.alfaordersbpm.domain.models.audit.Auditable;
import kz.alfabank.alfaordersbpm.domain.models.dto.IdentityDocumentDTO;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.identitydocument.IdentityDocument;
import kz.alfabank.alfaordersbpm.domain.models.identitydocument.IdentityDocumentType;
import kz.alfabank.alfaordersbpm.domain.models.mappers.OrderMapper;
import kz.alfabank.alfaordersbpm.rest.controllers.Constants;
import kz.alfabank.alfaordersbpm.domain.service.identitydocument.IdentityDocumentService;
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

@Api(value = "API беззалоговых заявок")
@CrossOrigin
@RestController
@RequestMapping(Constants.API_BASE + "/retail/v1/identitydocuments")
public class IdentityDocumentController {

    private static final Logger LOG = LoggerFactory.getLogger(IdentityDocumentController.class);

    private final IdentityDocumentService identityDocumentService;
    private final OrderMapper orderMapper;

    @Autowired
    public IdentityDocumentController(IdentityDocumentService identityDocumentService, OrderMapper orderMapper) {
        this.identityDocumentService = identityDocumentService;
        this.orderMapper = orderMapper;
    }

    @ApiOperation(value = "Документы удостоверяющие личность", notes = "Документы удостоверяющие личность")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Документы удостоверяющие личность заявки")
    @PreAuthorize("hasPermission(#orderId, 'OrderIdentityDoc', 'read')")
    public ResponseEntity<List<IdentityDocument>> getIdentityDocuments(@ApiParam(value = "ИД заявки", required = true) @Valid @RequestParam("orderId") Long orderId) {
        LOG.debug("REST request to getIdentityDocuments orderId={}", orderId);
        return ResponseEntity.ok(identityDocumentService.getByOrderId(orderId));
    }

    @ApiOperation(value = "Документ удостоверяющий личность", notes = "Документ удостоверяющий личность")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Документ удостоверяющий личность по ИД")
    @PreAuthorize("hasPermission(#id, 'IdentityDoc', 'read')")
    public ResponseEntity<IdentityDocument> getIdentityDocumentById(@ApiParam(value = "ИД записи", required = true) @PathVariable Long id) {
        LOG.debug("REST request to getIdentityDocumentById id={}", id);
        IdentityDocument result = identityDocumentService.getOne(id)
                                    .orElseThrow(() -> new EntityNotFoundException(String.format("Entity not found with ID = %s",id.toString())));
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Создать документ", notes = "Создать документ")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Создать документ удостоверяющий личность для заявки")
    @PreAuthorize("hasPermission(#documentDTO.orderId, 'OrderIdentityDoc', 'write')")
    public ResponseEntity<IdentityDocument> createIdentityDocument(@ApiParam(value = "документ", required = true) @Valid @RequestBody IdentityDocumentDTO documentDTO) {
        LOG.debug("REST request to createOrderIdentityDocument DTO={}", documentDTO);
        IdentityDocumentType documentType = IdentityDocumentType.parseFromString(documentDTO.getDocumentTypeRef().getValue());
        IdentityDocument identityDocument = orderMapper.fromDocumentDTO(documentDTO);
        identityDocument.setIdentityDocumentType(documentType);
        IdentityDocument result = identityDocumentService.insert(identityDocument);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri()
                )
                .body(result);
    }

    @ApiOperation(value = "Обновить документ", notes = "Обновить документ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Обновить документ удостоверяющий личность для заявки")
    @PreAuthorize("hasPermission(#identityDocument.orderId, 'OrderIdentityDoc', 'write')")
    public ResponseEntity<IdentityDocument> updateIdentityDocument(@ApiParam(value = "документ", required = true) @Valid @RequestBody IdentityDocument identityDocument) {
        LOG.debug("REST request to updateIdentityDocument {}", identityDocument);
        IdentityDocument result = identityDocumentService.update(identityDocument);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Удалить документ", notes = "Удалить документ")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Удалить документ удостоверяющий личность для заявки")
    @PreAuthorize("hasPermission(#id, 'IdentityDoc', 'write')")
    public ResponseEntity<Void> deleteIdentityDocumentById(@ApiParam(value = "ИД записи", required = true) @PathVariable Long id) {
        LOG.debug("REST request to deleteIdentityDocumentById id={}", id);
        identityDocumentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
