package kz.alfabank.alfaordersbpm.domain.repositories.dictionaries;

import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceCheck;
import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceCheckType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceCheckRepository extends JpaRepository<ServiceCheck, Long> {

    List<ServiceCheck> findByCheckType(ServiceCheckType serviceCheckType);
}
