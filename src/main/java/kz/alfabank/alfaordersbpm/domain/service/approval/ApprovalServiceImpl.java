package kz.alfabank.alfaordersbpm.domain.service.approval;

import kz.alfabank.alfaordersbpm.domain.models.approval.ApprovalModel;
import kz.alfabank.alfaordersbpm.domain.models.approval.FileCorrection;
import kz.alfabank.alfaordersbpm.domain.models.attachment.Attachment;
import kz.alfabank.alfaordersbpm.domain.models.attachment.AttachmentType;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.Task;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.TaskResult;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrderUiStep;
import kz.alfabank.alfaordersbpm.domain.repositories.ApprovalRepository;
import kz.alfabank.alfaordersbpm.domain.repositories.AttachmentRepository;
import kz.alfabank.alfaordersbpm.domain.repositories.RetailOrderRepository;
import kz.alfabank.alfaordersbpm.domain.service.bpmocrm.bpmadapter.BpmAdapterProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
@Transactional(timeout = 60, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final BpmAdapterProxy bpmAdapterProxy;
    private final FileCorrectionService fileCorrectionService;
    private final AttachmentRepository attachmentRepository;

    @Autowired
    private RetailOrderRepository retailOrderRepository;

    @Autowired
    public ApprovalServiceImpl(ApprovalRepository approvalRepository, BpmAdapterProxy bpmAdapterProxy, FileCorrectionService fileCorrectionService, AttachmentRepository attachmentRepository) {
        this.approvalRepository = approvalRepository;
        this.bpmAdapterProxy = bpmAdapterProxy;
        this.fileCorrectionService = fileCorrectionService;
        this.attachmentRepository = attachmentRepository;
    }

    private void setFileLastModified(ApprovalModel approvalModel, AttachmentType attachmentType) {
        FileCorrection fileCorrection = new FileCorrection();
        Attachment attachment = attachmentRepository.findByOrderIdAndAttachmentType(approvalModel.getOrderId(), attachmentType)
                .orElseThrow(() -> new EntityNotFoundException("Не найден приклепленный документ"));
        fileCorrection.setOrderId(approvalModel.getOrderId());
        fileCorrection.setAttachId(attachment);
        fileCorrection.setLastModified(attachment.getModifiedDate());
        fileCorrectionService.save(fileCorrection);
    }

    @Override
    public void saveApprovalFields(ApprovalModel approvalModel) {
        try {
            String processId = retailOrderRepository.getOrderProcessId(approvalModel.getOrderId());
            RetailOrderUiStep stepUI = RetailOrderUiStep.parseStepFromString(approvalModel.getService());
            Task task = bpmAdapterProxy.getTaskOrElseThrow(processId, stepUI.getStepName());

            if (approvalModel.getContent().getClientPhoto() != null)
                setFileLastModified(approvalModel, AttachmentType.CLIENT_PHOTO);
            if (approvalModel.getContent().getIdCardPhotoBack() != null)
                setFileLastModified(approvalModel, AttachmentType.IDCARD_BACKSIDE);
            if (approvalModel.getContent().getIdCardPhotoFront() != null)
                setFileLastModified(approvalModel, AttachmentType.IDCARD_FRONTSIDE);
            if (approvalModel.getContent().getSignedGCVPandPkb() != null)
                setFileLastModified(approvalModel, AttachmentType.SIGNED_PKB_GCVP);

            approvalRepository.saveApprovalFields(approvalModel);
            retailOrderRepository.setProcessingStepUI(approvalModel.getOrderId(), SecurityContextHolder.getContext().getAuthentication().getName());
            bpmAdapterProxy.completeTask(task, TaskResult.ofSuccess(task.getTkiid(), task.getName(), approvalModel.getDecision().getText()));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Ошибка параметров: "+ e.getMessage());
        } catch (SQLException e) {
            throw new IllegalArgumentException("Ошибка SQL: "+ e.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("Неопределенная ошибка: "+ e);
        }
    }
}
