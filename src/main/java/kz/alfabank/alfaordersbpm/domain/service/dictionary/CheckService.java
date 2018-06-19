package kz.alfabank.alfaordersbpm.domain.service.dictionary;

import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceCheck;
import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceCheckType;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValWebResponse;

import java.util.Optional;

public interface CheckService {

    DictValWebResponse getServiceCheckByType(ServiceCheckType serviceCheckType);

    Optional<ServiceCheck> getOne(Long id);

    ServiceCheck save(ServiceCheck entity);

    ServiceCheck update(ServiceCheck entity);

    ServiceCheck insert(ServiceCheck entity);

    boolean existsById(Long id);

    void delete(Long id);
}
