package kz.alfabank.alfaordersbpm.rest.controllers.signfinal;

import io.swagger.annotations.*;
import kz.alfabank.alfaordersbpm.domain.models.audit.Auditable;
import kz.alfabank.alfaordersbpm.domain.models.dto.SignFinalDTO;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.signfinal.SignFinal;
import kz.alfabank.alfaordersbpm.domain.repositories.SignFinalRepository;
import kz.alfabank.alfaordersbpm.domain.service.signfinal.SignFinalService;
import kz.alfabank.alfaordersbpm.rest.controllers.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Api(value = "API печати и подписания документов")
@CrossOrigin
@RestController
@RequestMapping(Constants.API_BASE + "/retail/v1/signfinal")
public class SignFinalContoller {
    private static final Logger LOG = LoggerFactory.getLogger(SignFinalContoller.class);

    private final SignFinalService signFinalService;

    @Autowired
    SignFinalRepository signFinalRepository;

    @Autowired
    public SignFinalContoller(SignFinalService signFinalService) {
        this.signFinalService = signFinalService;
    }

    @ApiOperation(value = "Данные печати и подписания", notes = "Данные печати и подписания")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/getdata", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Получение данных печати и подписания документов")
    public ResponseEntity<SignFinal> getOrderSignFinal(@ApiParam(value = "ИД заявки", required = true) @RequestParam("orderId") Long orderId) {
        LOG.debug("REST request to get signFinal OrderId={}", orderId);
        if (orderId == null)
            throw new BadRequestException("To getOrderSignFinal orderId must not be null");

        SignFinal result = signFinalService.getByOrderId(orderId);
        String segment = signFinalRepository.getClientSegment(orderId);
        result.setClientSegment(segment);

        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Создание кредитного договора", notes = "Создание кредитного договора")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PostMapping(value = "/createagr", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Создание кредитного договора")
    public ResponseEntity<SignFinal> createSignFinal(@ApiParam(value = "signFinal", required = true) @Valid @RequestBody SignFinalDTO signFinalDTO) {
        LOG.debug("REST request to createLoanAgreement {}", signFinalDTO);
        SignFinal result = signFinalService.createLoanAgreement(signFinalDTO);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Завершение подписания документов", notes = "Завершение подписания документов")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PostMapping(value = "/save/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Завершение подписания документов")
    public void saveSignFinal(@ApiParam(value = "ИД заявки", required = true) @PathVariable("orderId") Long orderId) {
        LOG.debug("REST to finish signFinal {}", orderId);
        signFinalService.saveSignFinal(orderId);
    }

}
