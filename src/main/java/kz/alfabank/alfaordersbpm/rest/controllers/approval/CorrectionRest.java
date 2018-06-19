package kz.alfabank.alfaordersbpm.rest.controllers.approval;

import io.swagger.annotations.*;
import kz.alfabank.alfaordersbpm.domain.models.approval.ApprovalModel;
import kz.alfabank.alfaordersbpm.domain.models.approval.CorrectionModel;
import kz.alfabank.alfaordersbpm.domain.models.attachment.AttachmentType;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrderUiStep;
import kz.alfabank.alfaordersbpm.domain.repositories.CorrectionRepository;
import kz.alfabank.alfaordersbpm.domain.service.approval.CorrectionService;
import kz.alfabank.alfaordersbpm.rest.controllers.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@Api(value = "API корректировки")
@RestController
@CrossOrigin
@RequestMapping(Constants.API_BASE + "/correct")
public class CorrectionRest {
    private static final Logger LOG = LoggerFactory.getLogger(CorrectionRest.class);
    private final CorrectionRepository correctionRepository;
    private final CorrectionService correctionService;

    @Autowired
    public CorrectionRest(CorrectionRepository correctionRepository, CorrectionService correctionService) {
        this.correctionRepository = correctionRepository;
        this.correctionService = correctionService;
    }

    @ApiOperation(value = "Сохранение откорректированных полей", notes = "Сохранение откорректированных полей")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PostMapping(value = "/save/{orderId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void saveFieldValues (
            @ApiParam(value = "orderId") @PathVariable Long orderId,
            @ApiParam(value = "body") @Valid @RequestBody ApprovalModel approvalModel
    ) {

        try {
            ApprovalModel approvalModelOLD = correctionRepository.getCorrectionData(orderId, approvalModel.getService(), true);

            if (approvalModel.getContent().getSignedGCVPandPkb() != null && !correctionService.isFileModified(orderId, AttachmentType.SIGNED_PKB_GCVP))
                throw new BadRequestException("Файл печати согласия ПКБ/ГЦВП не изменен");

            if (approvalModel.getContent().getClientPhoto() != null && !correctionService.isFileModified(orderId, AttachmentType.CLIENT_PHOTO))
                throw new BadRequestException("Фото клиента не изменено");

            if (approvalModel.getContent().getIdCardPhotoFront() != null && !correctionService.isFileModified(orderId, AttachmentType.IDCARD_FRONTSIDE))
                throw new BadRequestException("Лицевая сторона документа не изменена");

            if (approvalModel.getContent().getIdCardPhotoBack() != null && !correctionService.isFileModified(orderId, AttachmentType.IDCARD_BACKSIDE))
                throw new BadRequestException("Обратная сторона документа не изменена");

            if (approvalModel.getContent().checkModifiedFields(approvalModelOLD.getContent())) {
                throw new BadRequestException("Не все параметры изменены!");
            } else {
                List<CorrectionModel> fields = approvalModelOLD.getContent().compareFields(approvalModel.getContent());

                correctionService.completeCorrection(orderId, fields);
            }
        } catch (SQLException e) {
            LOG.error("Fields SQL error", e);
            throw new IllegalArgumentException("Ошибка сравнения: " + e.getMessage());
        } catch (BadRequestException e) {
            LOG.error("Fields save BadRequestException", e);
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            LOG.error("Fields save error", e);
            throw new IllegalArgumentException("saveFieldValues: "+e);
        }
    }

    @ApiOperation(value = "Поля для корректировки по службе", notes = "Поля для корректировки по службе")
    @GetMapping(value = "/getdata/{orderId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApprovalModel getData(
            @ApiParam(value = "ID заявки", required = true) @PathVariable Long orderId,
            @ApiParam(value = "Тип службы") @RequestParam(value = "service") String service,
            @ApiParam(value = "Отображение полей для корректировки")
            @RequestParam(value = "needContent", required = false) Boolean needContent
    ) {
        if (orderId == null)
            throw new BadRequestException("orderId must not be null");

        if (service != null && !(service.equals(RetailOrderUiStep.VERIFICATOR.getStepName()) ||
                service.equals(RetailOrderUiStep.SECURITY.getStepName()) ||
                service.equals(RetailOrderUiStep.RISKS.getStepName()) ||
                service.equals(RetailOrderUiStep.CREDITADMIN.getStepName()))) {
            throw new BadRequestException("service is incorrect");
        }

        try {
            return correctionRepository.getCorrectionData(orderId, service, needContent);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Ошибка параметров: "+ e.getMessage());
        } catch (SQLException e) {
            throw new IllegalArgumentException("Ошибка SQL: "+ e.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("Неопределенная ошибка: "+ e.getMessage());
        }
    }
}
