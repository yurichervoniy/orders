package kz.alfabank.alfaordersbpm.domain.repositories;

import kz.alfabank.alfaordersbpm.domain.models.Constants;
import kz.alfabank.alfaordersbpm.domain.models.order.OrderState;
import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrder;
import kz.alfabank.alfaordersbpm.domain.models.projections.order.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(timeout = 30, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public interface RetailOrderRepository extends CustomRepository<RetailOrder, Long> {

    Page<RetailOrder> findAllByActiveTrue(Pageable pageable);

    @Transactional(readOnly = true, timeout = 25)
    @Query(value = "SELECT t.id as id, t.orderNumber as orderNumber, t.orderDate as orderDate, t.startDate as startDate, t.creditProductRef as creditProductRef, t.requestedAmount as requestedAmount, t.loanDuration as loanDuration, t.iin as iin, t.lastName as lastName, t.firstName as firstName, t.middleName as middleName, t.active as active, t.cancelable as cancelable, t.stepUI as stepUI, t.piid as piid FROM AbstractOrder t where t.orderDate>=?3 and t.orderDate<=?4 and t.active=?5" + Constants.ORDER_PERMISSION
     , countQuery = "SELECT count(1) FROM AbstractOrder t where t.orderDate>=?3 and t.orderDate<=?4 and t.active=?5" + Constants.ORDER_PERMISSION)
    Page<MyRetailOrders> findAllMyActiveFlag(List<String> grantees, String orgCode, LocalDate startDate, LocalDate endDate, boolean isActive, Pageable pageable);


    @Transactional(readOnly = true, timeout = 25)
    @Query(value = "SELECT t.id as id, t.orderNumber as orderNumber, t.orderDate as orderDate, t.startDate as startDate, t.creditProductRef as creditProductRef, t.requestedAmount as requestedAmount, t.loanDuration as loanDuration, t.iin as iin, t.lastName as lastName, t.firstName as firstName, t.middleName as middleName, t.active as active, t.cancelable as cancelable, t.stepUI as stepUI, t.piid as piid FROM AbstractOrder t where t.orderDate>=?3 and t.orderDate<=?4" + Constants.ORDER_PERMISSION
     , countQuery = "SELECT count(1) FROM AbstractOrder t where t.orderDate>=?3 and t.orderDate<=?4" + Constants.ORDER_PERMISSION)
    Page<MyRetailOrders> findAllMy(List<String> grantees, String orgCode, LocalDate startDate, LocalDate endDate, Pageable pageable);


    @Transactional(readOnly = true, timeout = 25)
    @Query(value = "SELECT t.id as id, t.orderNumber as orderNumber, t.orderDate as orderDate, t.startDate as startDate, t.creditProductRef as creditProductRef, t.requestedAmount as requestedAmount, t.loanDuration as loanDuration, t.iin as iin, t.lastName as lastName, t.firstName as firstName, t.middleName as middleName, t.active as active, t.cancelable as cancelable, t.stepUI as stepUI, t.piid as piid FROM AbstractOrder t where t.orderState=?5 and t.orderDate>=?3 and t.orderDate<=?4" + Constants.ORDER_PERMISSION
    , countQuery =  "SELECT count(1) as piid FROM AbstractOrder t where t.orderState=?5 and t.orderDate>=?3 and t.orderDate<=?4" + Constants.ORDER_PERMISSION)
    Page<MyRetailOrders> pageAllByOrderState(List<String> grantees, String orgCode, LocalDate startDate, LocalDate endDate, OrderState orderState, Pageable pageable);


    @Transactional(readOnly = true)
    Optional<AlfrescoOrder> findByIdEquals(Long orderId);

    @Transactional(readOnly = true)
    Optional<ProcAppInfo> findByRequestId(Long requestId);

    @Query(value = "select getOrderNum(?1) from dual", nativeQuery = true)
    Optional<String> getOrderNum(String orderType);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update orders o set o.is_active=?2 where o.id=?1", nativeQuery = true)
    void updateIsActive(Long id, Boolean active);

    @Transactional(readOnly = true)
    @Query(value = "select t.id as id, to_char(t.stamp,'YYYY-MM-DD\"T\"HH24:MI:SS') as stamp, t.sessionuser as dbuser, t.username as username, t.orderid as orderid, t.entityname as entityname, t.entityid as entityid, t.operation as operation from AUDIT_ORDER_TBL t where t.orderid=?1 order by t.id", nativeQuery = true)
    List<OrderAuditOperations> getOrderAuditOperations(Long id);

    @Transactional(readOnly = true)
    @Query(value = "select t.id as id, t.audit_id as auditid, t.propertyname as propertyname, t.old_value as oldvalue, t.new_value as newvalue from AUDIT_ORDER_TBL_DETAILS t where t.audit_id=?1 order by id", nativeQuery = true)
    List<OrderAuditDetails> getOrderAuditDetails(Long auditId);

    @Procedure(name = "createProcAppRequest")
    @Transactional(timeout = 20)
    void createProcAppRequest(@Param("p_orderid") Long orderId, @Param("p_instanceid") String instanceid, @Param("p_createdby") String createdby, @Param("p_step") String step);

    @Transactional(readOnly = true, timeout = 10)
    @Query(value = "select o.process_id from orders o where o.id=?1", nativeQuery = true)
    String getOrderProcessId(Long id);

    @Transactional(readOnly = true, timeout = 15)
    @Query(value = "select o.ui_step from orders o where o.id=?1", nativeQuery = true)
    String getOrderUiStep(Long id);

    @Transactional(readOnly = true, timeout = 15)
    @Query(value = "select o.id as id, o.orderNumber as orderNumber, o.orderDate as orderDate, o.stepUI as stepUI, o.orgName as orgName, o.orderState as orderState, o.piid as piid from AbstractOrder o where o.id = ?1")
    Optional<MainInfo> findOrderMainInfo(Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update orders o set o.ui_step='Обработка: '||o.ui_step, o.modified_by=?2 where o.id=?1", nativeQuery = true)
    void setProcessingStepUI(Long id, String userName);

    @Transactional(readOnly = true, timeout = 15)
    @Query("select o.iin as iin, o.lastName as lastName, o.firstName as firstName, o.middleName as middleName, o.birthDate as birthDate, o.genderRef as genderRef, o.gender as gender from AbstractOrder o where o.id=?1")
    Optional<SubjectInfo> findOrderSubjectInfo(Long id);

    @Transactional(readOnly = true, timeout = 15)
    @Query("select o.orderNumber as orderNumber, o.orderDate as orderDate, o.creditProductRef as creditProductRef, o.paymentTypeRef as paymentTypeRef, o.paymentType as paymentType, o.orgCode as orgCode, o.orgName as orgName, o.insuranceRef as insuranceRef from AbstractOrder o where o.id=?1")
    Optional<OrderInfo> findOrderInfo(Long id);

}
