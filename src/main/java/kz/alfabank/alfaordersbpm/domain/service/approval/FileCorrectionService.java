package kz.alfabank.alfaordersbpm.domain.service.approval;

import kz.alfabank.alfaordersbpm.domain.models.approval.FileCorrection;
import kz.alfabank.alfaordersbpm.domain.models.attachment.AttachmentType;

import java.util.Optional;

public interface FileCorrectionService {
    Optional<FileCorrection> getOne(Long id);

    FileCorrection save(FileCorrection entity);

    void delete(Long id);
}
