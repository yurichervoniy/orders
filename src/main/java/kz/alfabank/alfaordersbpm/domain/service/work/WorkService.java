package kz.alfabank.alfaordersbpm.domain.service.work;

import kz.alfabank.alfaordersbpm.domain.models.dto.WorkDetailsDTO;
import kz.alfabank.alfaordersbpm.domain.models.work.Work;

import java.util.Optional;

public interface WorkService {
    Optional<Work> getOne(Long id);

    Work save(Work entity);

    Work update(Work entity);

    Work insert(Work entity);

    Work fromWorkDetails(Long orderId,WorkDetailsDTO workDetailsDTO);

    boolean existsById(Long id);

    void delete(Long id);

    Optional<Work> getByOrderId(Long orderId);
}
