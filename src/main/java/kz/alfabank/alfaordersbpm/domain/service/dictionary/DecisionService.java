package kz.alfabank.alfaordersbpm.domain.service.dictionary;


import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceDecision;
import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceDecisionType;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValWebResponse;

import java.util.Optional;

public interface DecisionService {

    DictValWebResponse getServiceDecisionByType(ServiceDecisionType serviceDecisionType);

    Optional<ServiceDecision> getOne(Long id);

    ServiceDecision save(ServiceDecision entity);

    ServiceDecision update(ServiceDecision entity);

    ServiceDecision insert(ServiceDecision entity);

    boolean existsById(Long id);

    void delete(Long id);
}
