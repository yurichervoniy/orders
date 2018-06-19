package kz.alfabank.alfaordersbpm.domain.service.identitydocument;


import kz.alfabank.alfaordersbpm.domain.models.identitydocument.IdentityDocument;

import java.util.List;
import java.util.Optional;

public interface IdentityDocumentService {

    List<IdentityDocument> getByOrderId(Long orderId);

    Optional<IdentityDocument> getOne(Long id);

    IdentityDocument save(IdentityDocument entity);

    IdentityDocument update(IdentityDocument entity);

    IdentityDocument insert(IdentityDocument entity);

    boolean existsById(Long id);

    void delete(Long id);

}
