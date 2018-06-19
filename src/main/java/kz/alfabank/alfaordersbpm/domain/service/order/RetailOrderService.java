package kz.alfabank.alfaordersbpm.domain.service.order;

import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.Task;
import kz.alfabank.alfaordersbpm.domain.models.dto.ApprovedConditionsDTO;
import kz.alfabank.alfaordersbpm.domain.models.dto.PersonDetailsDTO;
import kz.alfabank.alfaordersbpm.domain.models.dto.PilOrderConditionDTO;
import kz.alfabank.alfaordersbpm.domain.models.order.OrderInProgress;
import kz.alfabank.alfaordersbpm.domain.models.order.OrderStateFilter;
import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrder;
import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrderUiStep;
import kz.alfabank.alfaordersbpm.domain.models.projections.order.MyRetailOrders;
import kz.alfabank.alfaordersbpm.domain.models.projections.order.OrderAuditDetails;
import kz.alfabank.alfaordersbpm.domain.models.projections.order.OrderAuditOperations;
import kz.alfabank.alfaordersbpm.domain.models.projections.order.ProcAppInfo;
import kz.alfabank.alfaordersbpm.domain.models.scoring.ApprovedConditions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RetailOrderService {

    Page<MyRetailOrders> pageMyOrders(LocalDate startDate, LocalDate endDate, OrderStateFilter stateFilter, Pageable pageable);

    Optional<RetailOrder> getOne(Long id);

    Optional<ProcAppInfo> getByRequestId(Long requestId);

    Optional<RetailOrder> getOne(RetailOrder entity);

    RetailOrder save(RetailOrder entity);

    RetailOrder update(RetailOrder entity);

    RetailOrder insert(RetailOrder entity);

    RetailOrder fromCondition(PilOrderConditionDTO conditionDTO);

    RetailOrder updatePersonDetails(Long id, PersonDetailsDTO personDetailsDTO);

    boolean existsById(Long id);

    void delete(RetailOrder entity);

    void delete(Long id);

    Optional<OrderInProgress> findOrderInProgress(String iin);

    List<OrderAuditOperations> getAuditOperations(Long id);

    List<OrderAuditDetails> getOrderAuditDetails(Long auditId);

    String getProcessStepUI(RetailOrder entity);

    Task getOrderTask(RetailOrder entity, RetailOrderUiStep uiStep);

    Optional<ApprovedConditions> getApprovedConditions(Long id);

    ApprovedConditions fromApprovedConditionsDTO(Long id, ApprovedConditionsDTO conditionsDTO);

}
