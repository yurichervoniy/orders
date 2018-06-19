package kz.alfabank.alfaordersbpm.rest.controllers.approval;

import io.swagger.annotations.*;
import kz.alfabank.alfaordersbpm.domain.models.approval.ApprovalModel;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrderUiStep;
import kz.alfabank.alfaordersbpm.domain.repositories.ApprovalRepository;
import kz.alfabank.alfaordersbpm.domain.service.approval.ApprovalService;
import kz.alfabank.alfaordersbpm.rest.controllers.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@Api(value = "API верификации")
@RestController
@CrossOrigin
@RequestMapping(Constants.API_BASE + "/approval")
public class ApprovalRest {

    private final ApprovalService approvalService;
    private final ApprovalRepository approvalRepository;

    @Autowired
    public ApprovalRest(ApprovalService approvalService, ApprovalRepository approvalRepository) {
        this.approvalService = approvalService;
        this.approvalRepository = approvalRepository;
    }

    @ApiOperation(value = "Сохранение полей для корректировки", notes = "Сохранение полей для корректировки")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PostMapping(value = "/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveFields (
            @ApiParam(value = "body") @Valid @RequestBody ApprovalModel approvalModel
            ) {
        if (approvalModel.getOrderId() == null)
            throw new BadRequestException("orderId must not be null");

        if (approvalModel.getDecision() == null)
            throw new BadRequestException("decision must not be null");

        if (approvalModel.getService() == null)
            throw new BadRequestException("service must not be null");

        if (approvalModel.getService().equals(RetailOrderUiStep.VERIFICATOR.getStepName()) ||
                approvalModel.getService().equals(RetailOrderUiStep.SECURITY.getStepName()) ||
                approvalModel.getService().equals(RetailOrderUiStep.RISKS.getStepName()) ||
                approvalModel.getService().equals(RetailOrderUiStep.CREDITADMIN.getStepName())) {
            approvalService.saveApprovalFields(approvalModel);
        } else {
            throw new BadRequestException("service is incorrect");
        }
    }

    @ApiOperation(value = "История комментариев, оставленных службами", notes = "История комментариев, оставленных службами")
    @GetMapping(value = "/gethistory/{orderId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ApprovalModel> getData(
            @ApiParam(value = "ID заявки", required = true) @PathVariable Long orderId,
            @ApiParam(value = "Тип службы", required = true)
            @RequestParam(value = "service") String service,
            @ApiParam(value = "Отображение полей для корректировки")
            @RequestParam(value = "needContent", required = false) Boolean needContent
    ) {
        if (orderId == null)
            throw new BadRequestException("orderId must not be null");

        if (service == null)
            throw new BadRequestException("service must not be null");

        if (service.equals(RetailOrderUiStep.VERIFICATOR.getStepName()) ||
                service.equals(RetailOrderUiStep.SECURITY.getStepName()) ||
                service.equals(RetailOrderUiStep.RISKS.getStepName()) ||
                service.equals(RetailOrderUiStep.CREDITADMIN.getStepName())) {
            try {
                return approvalRepository.getHistory(orderId, service, needContent);
            } catch (EntityNotFoundException e) {
                throw new EntityNotFoundException("Ошибка параметров: "+ e.getMessage());
            } catch (SQLException e) {
                throw new IllegalArgumentException("Ошибка SQL: "+ e.getMessage());
            } catch (Exception e) {
                throw new IllegalArgumentException("Неопределенная ошибка: "+ e.getMessage());
            }
        } else {
            throw new BadRequestException("service is incorrect");
        }
    }
}
