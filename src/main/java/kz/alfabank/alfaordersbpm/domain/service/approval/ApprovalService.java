package kz.alfabank.alfaordersbpm.domain.service.approval;

import kz.alfabank.alfaordersbpm.domain.models.approval.ApprovalModel;

public interface ApprovalService {
    void saveApprovalFields(ApprovalModel approvalModel);
}
