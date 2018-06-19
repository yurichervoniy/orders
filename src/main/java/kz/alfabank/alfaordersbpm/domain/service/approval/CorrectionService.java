package kz.alfabank.alfaordersbpm.domain.service.approval;

import kz.alfabank.alfaordersbpm.domain.models.approval.CorrectionModel;
import kz.alfabank.alfaordersbpm.domain.models.attachment.AttachmentType;

import java.util.List;

public interface CorrectionService {
    Boolean updateFields(Long orderId, List<CorrectionModel> fields);

    void completeCorrection(Long orderId, List<CorrectionModel> fields);

    boolean isFileModified(Long orderId, AttachmentType attachmentType);
}
