package kz.alfabank.alfaordersbpm.domain.repositories;

import kz.alfabank.alfaordersbpm.domain.models.attachment.Attachment;
import kz.alfabank.alfaordersbpm.domain.models.attachment.AttachmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByOrderId(Long orderId);

    Optional<Attachment> findByOrderIdAndAttachmentType(Long orderId, AttachmentType attachmentType);

    Integer countAllByOrderIdAndAttachmentTypeIn(Long orderId, List<AttachmentType> attachmentTypes);

}
