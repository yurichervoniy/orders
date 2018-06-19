package kz.alfabank.alfaordersbpm.domain.repositories;

import kz.alfabank.alfaordersbpm.domain.models.identitydocument.IdentityDocument;
import kz.alfabank.alfaordersbpm.domain.models.identitydocument.IdentityDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IdentityDocumentRepository extends JpaRepository<IdentityDocument, Long> {

    List<IdentityDocument> findByOrderId(Long orderId);

    Optional<IdentityDocument> findByOrderIdAndIdentityDocumentType(Long orderId, IdentityDocumentType identityDocumentType);

}
