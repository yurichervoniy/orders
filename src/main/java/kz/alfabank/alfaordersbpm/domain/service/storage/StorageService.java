package kz.alfabank.alfaordersbpm.domain.service.storage;

import kz.alfabank.alfaordersbpm.domain.models.attachment.Attachment;
import kz.alfabank.alfaordersbpm.domain.models.attachment.AttachmentType;
import kz.alfabank.alfaordersbpm.domain.models.dto.StorageContent;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface StorageService {

    Optional<Attachment> getOne(Long id);

    Optional<Attachment> getOrderAttachment(Long orderId, AttachmentType attachmentType);

    Attachment storeOrderFile(Long orderId, AttachmentType attachmentType, MultipartFile file) throws IOException;

    StorageContent getFileContent(Long id);

    StorageContent getOrderFileContent(Long orderId, AttachmentType attachmentType);

    void completePassportPhoto(Long orderId);

}
