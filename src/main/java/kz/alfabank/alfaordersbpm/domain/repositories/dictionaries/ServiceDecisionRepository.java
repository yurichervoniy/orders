package kz.alfabank.alfaordersbpm.domain.repositories.dictionaries;

import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceDecision;
import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceDecisionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceDecisionRepository extends JpaRepository<ServiceDecision, Long>{
    List<ServiceDecision> findByDecisionType(ServiceDecisionType serviceDecisionType);
}