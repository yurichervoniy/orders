package kz.alfabank.alfaordersbpm.rest.controllers.orders;

import io.swagger.annotations.*;
import kz.alfabank.alfaordersbpm.domain.models.additionalinfo.AdditionalInfo;
import kz.alfabank.alfaordersbpm.domain.models.audit.Auditable;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.Task;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.TaskAction;
import kz.alfabank.alfaordersbpm.domain.models.dto.*;
import kz.alfabank.alfaordersbpm.domain.models.order.*;
import kz.alfabank.alfaordersbpm.domain.models.projections.order.*;
import kz.alfabank.alfaordersbpm.domain.models.scoring.ApprovedConditions;
import kz.alfabank.alfaordersbpm.domain.models.work.Work;
import kz.alfabank.alfaordersbpm.domain.repositories.OrderPermissionRepository;
import kz.alfabank.alfaordersbpm.domain.repositories.RetailOrderRepository;
import kz.alfabank.alfaordersbpm.domain.service.bpmocrm.bpmadapter.BpmAdapterProxy;
import kz.alfabank.alfaordersbpm.rest.controllers.Constants;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.service.additionalinfo.AdditionalInfoService;
import kz.alfabank.alfaordersbpm.domain.service.order.RetailOrderProperties;
import kz.alfabank.alfaordersbpm.domain.service.order.RetailOrderService;
import kz.alfabank.alfaordersbpm.domain.service.smsconfirmation.SmsConfirmationService;
import kz.alfabank.alfaordersbpm.domain.service.work.WorkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Api(value = "API беззалоговых заявок")
@CrossOrigin
@RestController
@RequestMapping(Constants.API_BASE + "/retail/v1/orders")
@Validated
public class RetailOrderController {

    private static final Logger LOG = LoggerFactory.getLogger(RetailOrderController.class);
    private static final String DEFAULT_PRODUCE_MEDIA_TYPE = MediaType.APPLICATION_JSON_VALUE;
    private static final String ENTITY_NOT_FOUND = "Entity not found with ID = %s";
    private static final String ACRONYM = "SCD_BPM";
    private static final String READ_PERMISSION = "READ";
    private static final List<String> SERVICES = Arrays.asList(RetailOrderUiStep.VERIFICATOR.getStepName(), RetailOrderUiStep.SECURITY.getStepName()
                                        , RetailOrderUiStep.RISKS.getStepName(), RetailOrderUiStep.CREDITADMIN.getStepName());

    @Autowired
    private RetailOrderService retailOrderService;

    @Autowired
    private SmsConfirmationService confirmationService;

    @Autowired
    private RetailOrderProperties orderProperties;

    @Autowired
    private WorkService workService;

    @Autowired
    private AdditionalInfoService additionalInfoService;

    @Autowired
    private BpmAdapterProxy bpmAdapterProxy;

    @Autowired
    private RetailOrderRepository orderRepository;

    @Autowired
    private OrderPermissionRepository permissionRepository;


    @ApiOperation(value = "Заявки пользователя", notes = "Возвращает список заявок пользователя", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получение списка моих заявок")
    @PreAuthorize("hasPermission(null, 'MyOrders', 'read')")
    public ResponseEntity<Page<MyRetailOrders>> pageMyPilOrders(@ApiParam(name = "startDate", value = "Дата начала", required = true) @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                @ApiParam(name = "endDate", value = "Дата окончания", required = true) @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                                @ApiParam(name = "stateFilter", value = "Состояние") @RequestParam(name = "stateFilter") OrderStateFilter stateFilter,
                                                                @ApiParam(name = "pageable", value = "Параметры пагинации") Pageable pageable) {
        LOG.debug("REST request to page all entities {}", pageable);
        return ResponseEntity.ok(retailOrderService.pageMyOrders(startDate, endDate, stateFilter, pageable));
    }

    @ApiOperation(value = "Получить заявку", notes = "Возвращает информацию о заявке по ID", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/{id}", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получение заявки по ИД")
    @OrderReadPermission
    public ResponseEntity<RetailOrder> getPilOrderById(@ApiParam(value = "ID заявки", required = true)@PathVariable Long id) {
        LOG.debug("REST request to getPilOrderById id={}", id);
        Optional<RetailOrder> optional =  retailOrderService.getOne(id);
        if (!optional.isPresent())
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND,id.toString()));
        return ResponseEntity.ok(optional.get());
    }

    @ApiOperation(value = "Получить заявку proc app", notes = "Получить заявку proc app", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/procapp", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получение route procapp")
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public ResponseEntity<ProcAppInfo> routeProcApp(
            @ApiParam(value = "ProcessId", required = true) @RequestParam(name = "processId") String processId,
            @ApiParam(value = "TaskId", required = true) @RequestParam(name = "taskId") String taskId,
            @ApiParam(value = "RequestId PROCAPP", required = true) @RequestParam(name = "requestId") Long requestId
    ) {
        LOG.debug("REST request to getProcApp processId={} taskId={} requestId={}", processId, taskId, requestId);
        Task task = bpmAdapterProxy.getTaskById(taskId);
        ProcAppInfo procAppInfo =  retailOrderService.getByRequestId(requestId)
                                    .orElseThrow(() -> new EntityNotFoundException(String.format("Order with RequestId=%s not found", requestId.toString())));

        if (Task.ASSIGNED_TO_GROUP.equalsIgnoreCase(task.getAssignedToType()) && Task.RECEIVED_STATUS.equalsIgnoreCase(task.getStatus())
                && task.getCompletionTime() == null && SERVICES.contains(task.getName()) ){
            permissionRepository.addOrderPermission(procAppInfo.getId(), READ_PERMISSION, SecurityContextHolder.getContext().getAuthentication().getName());
            bpmAdapterProxy.setTaskStatus(task, TaskAction.CLAIM);
            //TODO SEARCH FOR EARLY TASKS NOT ASSIGNED. USER MUST NOT HAVE TASK IN PROGRESS
        }

        return ResponseEntity.ok(new ProcAppInfoDTO(procAppInfo, task.getName()));
    }

    @ApiOperation(value = "Создать заявку только с условиями", notes = "Создает заявку только по условиям", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PostMapping(value = "/fromcondition", produces = DEFAULT_PRODUCE_MEDIA_TYPE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Создание заявки")
    @OrderCreatePermission
    public ResponseEntity<RetailOrder> fromcondition(@ApiParam(value = "условия заявки", required = true) @Valid @RequestBody PilOrderConditionDTO conditionDTO) {
        LOG.debug("REST request to updatePilOrderPersonDetails Body={}", conditionDTO);

        RetailOrder result = retailOrderService.fromCondition(conditionDTO);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri())
                .body(result);
    }

    @ApiOperation(value = "Обновить заявку деталями персоны", notes = "Обновить заявку деталями персоны", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PostMapping(value = "/{id}/persondetails", produces = DEFAULT_PRODUCE_MEDIA_TYPE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Обновление заявки из данных PersonDetailsDTO")
    @OrderWritePermission
    public ResponseEntity<RetailOrder> updatePilOrderPersonDetails(@ApiParam(value = "ИД заявки", required = true) @PathVariable Long id,
                                                                   @Valid @RequestBody PersonDetailsDTO personDetailsDTO) {
        LOG.debug("REST request to updatePilOrderPersonDetails orderid={} DTO={}", id, personDetailsDTO);

        RetailOrder result = retailOrderService.updatePersonDetails(id,personDetailsDTO);
        return ResponseEntity.ok(result);
    }


    @ApiOperation(value = "Удалить заявку", notes = "Удалить заявку по ID", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @DeleteMapping(value = "/{id}", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Удаление заявки по ИД")
    @PreAuthorize("hasPermission(#id, 'RetailOrder', 'delete')")
    public ResponseEntity<Void> deleteEntity(@ApiParam(value = "ID заявки", required = true) @PathVariable Long id) {
        LOG.debug("REST request to deleteEntity id={}",id);
        retailOrderService.delete(id);
        return ResponseEntity.noContent()
                .build();
    }


    @GetMapping(value = "/{id}/state")
    @Auditable(eventName = "Получение UI_STEP заявки по ИД")
    public OrderStateInfo getState(@PathVariable Long id) {
        LOG.debug("REST request to getState orderId={}", id);
        String step = orderRepository.getOrderUiStep(id);
        if (step == null)
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, id.toString()));
        return new OrderStateInfo(step, step);
    }


    @GetMapping(value = "/{id}/task/{taskName}")
    public Optional<Task> getTask(@PathVariable Long id, @PathVariable String taskName) {
        LOG.debug("REST request to getTask orderId={} taskName={}", id, taskName);
        Optional<RetailOrder> optional = retailOrderService.getOne(id);
        if (!optional.isPresent())
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, id.toString()));
        RetailOrder order = optional.get();
        return bpmAdapterProxy.getReceivedTaskByName(order.getPiid(), taskName);
    }

    @ApiOperation(value = "СМС потдверждение номера", notes = "СМС потдверждение номера", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PostMapping(value = "/{id}/smsconformations", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "СМС потдверждение номера для заявки")
    @OrderWritePermission
    public PhoneConfirmationResponse createConfirmation(@ApiParam(value = "ID заявки", required = true) @PathVariable Long id, @ApiParam(value = "Параметры", required = true) @Valid @RequestBody PhoneConfirmation confirmation) {
        LOG.debug("REST request to sendConfirmation orderId={}, PhoneConfirmation={}", id, confirmation);
        return confirmationService.sendSmsConfirmation(id, confirmation);
    }

    @ApiOperation(value = "Проверка кода потдверждения", notes = "Проверка кода потдверждения", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/{id}/smsconformations/check", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Проверка кода потдверждения СМС для заявки")
    @OrderWritePermission
    public PhoneConfirmationCheck checkConfirmation(@ApiParam(value = "ID заявки", required = true) @PathVariable Long id, @ApiParam(value = "Телефон", required = true) @RequestParam("phone") String phone, @ApiParam(value = "Код из СМС", required = true) @RequestParam("code") String code) {
        LOG.debug("REST request to checkConfirmation  phone={}, code={}", phone, code);
        return confirmationService.checkSmsConfirmation(id, phone, code);
    }

    @ApiOperation(value = "Получить ключ-значение свойств заявки", notes = "Проверка кода потдверждения", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/{id}/properties", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получить ключ-значение для заявки")
    @OrderReadPermission
    public Map<String, String> getOrderPropertiesAsMap(@ApiParam(value = "ID заявки", required = true) @PathVariable Long id) throws SQLException {
        LOG.debug("REST request to getOrderPropertiesAsMap orderId={}", id);
        if (id == null )
            throw new BadRequestException("To getOrderPropertiesAsMap orderId must not be null");
        return orderProperties.getOrderProperties(id);
    }

    @ApiOperation(value = "Добавление данных по работе", notes = "Добавление данных по работе", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PostMapping(value = "/{id}/workdetails", produces = DEFAULT_PRODUCE_MEDIA_TYPE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Добавление данных о работе для заявки")
    @OrderWritePermission
    public ResponseEntity<Work> createWorkFromDetails(@ApiParam(value = "ID заявки и данные по работе", required = true) @PathVariable Long id, @Valid @RequestBody WorkDetailsDTO workDetailsDTO) {
        LOG.debug("REST request to update an entity from workDetailsDTO={}", workDetailsDTO);

        Work result = workService.fromWorkDetails(id,workDetailsDTO);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Получение данных по работе", notes = "Получение данных по работе", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/{id}/workdetails", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получение данных о работе")
    @OrderReadPermission
    public ResponseEntity<Work> getWorkDetails(@ApiParam(value = "ID заявки и данные по работе", required = true) @PathVariable Long id) {
        LOG.debug("REST request to getWorkDetails orderId={}",id);
        if (id == null)
            throw new BadRequestException("getWorkDetails id is null");

        Work result = workService.getByOrderId(id)
                .orElseThrow(()-> new EntityNotFoundException(String.format("WorkDetails not found for order with id=%s", id)));
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Добавление дополнительных данных", notes = "Добавление дополнительных данных", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PostMapping(value = "/{id}/addinfo", produces = DEFAULT_PRODUCE_MEDIA_TYPE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Добавление дополнительных данных")
    @OrderWritePermission
    public ResponseEntity<AdditionalInfo> createAddInfo(@ApiParam(value = "ID заявки", required = true) @PathVariable Long id,
                                                        @ApiParam(value = "Доп сведения", required = true) @Valid @RequestBody AdditionalInfoDTO infoDTO) {
        LOG.debug("REST request to createAddInfo orderId={} {}", id, infoDTO);

        AdditionalInfo result = additionalInfoService.fromAddInfoDTO(id, infoDTO);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Поулчить доп. сведенеия", notes = "Поулчить доп. сведенеия", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/{id}/addinfo", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Поулчение дополнительных данных")
    @OrderReadPermission
    public ResponseEntity<AdditionalInfo> getAddInfo(@ApiParam(value = "ID заявки", required = true) @PathVariable Long id) {
        LOG.debug("REST request to getAddInfo orderId={}",id);
        if (id == null)
            throw new BadRequestException("getAddInfo orderId is null");

        AdditionalInfo result = additionalInfoService.getByOrderId(id)
                .orElseThrow(()-> new EntityNotFoundException(String.format("AdditionalInfo not found for order with id=%s", id)));
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Получить активную заявку по ИИН", notes = "Получить активную заявку по ИИН")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/findactive", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Поиск активной заявки по ИИН")
    public Optional<OrderInProgress> findactive(@ApiParam(value = "ИИН", required = true) @RequestParam("iin") String iin){
        LOG.debug("REST findactive iin=[{}]", iin);
        return retailOrderService.findOrderInProgress(iin);
    }


    @ApiOperation(value = "Получить аудит операций для заявки", notes = "Получить аудит операций для заявки")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/{id}/auditoperations", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получение аудита операций для заявки")
    @OrderAuditPermission
    public List<OrderAuditOperations> findAuditOperations(@ApiParam(value = "ID заявки", required = true) @PathVariable Long id){
        LOG.debug("REST findAuditOperations orderId=[{}]", id);
        if (id == null)
            throw new BadRequestException("findAuditOperations orderId is null");
        return retailOrderService.getAuditOperations(id);
    }

    @ApiOperation(value = "Получить детали аудита", notes = "Получить детали аудита")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/auditdetails/{auditid}", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @OrderAuditPermission
    public List<OrderAuditDetails> findAuditDetails(@ApiParam(value = "ID записи аудита", required = true) @PathVariable Long auditid){
        LOG.debug("REST findAuditDetails auditId=[{}]", auditid);
        if (auditid == null)
            throw new BadRequestException("findAuditDetails auditid is null");
        return retailOrderService.getOrderAuditDetails(auditid);
    }

    @ApiOperation(value = "Получить ключ-значение данных по заявке для служб", notes = "Получить ключ-значение данных по заявке для служб", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/{id}/services/info", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получить ключ-значение для служб")
    @OrderReadPermission
    public Map<String, String> getOrderServiceInfoAsMap(@ApiParam(value = "ID заявки", required = true) @PathVariable Long id) throws SQLException {
        LOG.debug("REST request to getOrderServiceInfoAsMap orderId={}", id);
        if (id == null )
            throw new BadRequestException("To getOrderServiceInfoAsMap orderId must not be null");
        return orderProperties.getOrderServiceInfo(id);
    }


    @ApiOperation(value = "Получить одобренные условия", notes = "Получить одобренные условия", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/{id}/approvedconditions", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получение одобренных условий")
    @OrderReadPermission
    public ResponseEntity<ApprovedConditions> setApprovedConditions(@ApiParam(value = "ID заявки", required = true) @PathVariable Long id) {
        LOG.debug("REST request to getApprovedConditions orderId={}", id);
        Optional<ApprovedConditions> optional = retailOrderService.getApprovedConditions(id);
        if (!optional.isPresent())
            throw new EntityNotFoundException(String.format("ApprovedConditions not found for orderId = %s", id.toString()));
        return ResponseEntity.ok(optional.get());
    }

    @ApiOperation(value = "Дополнить одобренные условия", notes = "Дополнить одобренные условия", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PostMapping(value = "/{id}/approvedconditions", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Решение банка и клиента")
    @OrderWritePermission
    public ResponseEntity<ApprovedConditions> updateApprovedConditions(@ApiParam(value = "ID заявки", required = true) @PathVariable Long id,
                                    @ApiParam(value = "Доп сведения", required = true) @Valid @RequestBody ApprovedConditionsDTO conditionsDTO) {
        LOG.debug("REST request to updateApprovedConditions orderId={}", id);
        ApprovedConditions conditions = retailOrderService.fromApprovedConditionsDTO(id, conditionsDTO);
        return ResponseEntity.ok(conditions);
    }

    @ApiOperation(value = "Основные параметры для страницы", notes = "Основные параметры для страницы", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/{id}/maininfo", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    public ResponseEntity<MainInfo> getOrderMainInfo(@ApiParam(value = "ID заявки", required = true) @PathVariable Long id) {
        LOG.debug("REST request to getOrderMainInfo orderId={}", id);
        MainInfo mainInfo = orderRepository.findOrderMainInfo(id)
                                        .orElseThrow(()-> new EntityNotFoundException(String.format("MainInfo not found for orderId = %s", id.toString())));
        return ResponseEntity.ok(mainInfo);
    }

    @ApiOperation(value = "Есть ли разрешение на запуск процесса PIL", notes = "Есть ли разрешение на запуск процесса PIL", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/exposetostart", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    public ResponseEntity<Boolean> exposeToStart() {
        LOG.debug("REST request to exposeToStart");
        boolean canStart = bpmAdapterProxy.exposeToStartByAcronym(ACRONYM);
        return ResponseEntity.ok(canStart);
    }

    @ApiOperation(value = "Информация по субъекту заявки", notes = "Информация по субъекту заявки", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/{id}/subjectinfo", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @OrderReadPermission
    public ResponseEntity<SubjectInfo> getOrderSubjectInfo(@ApiParam(value = "ID заявки", required = true) @PathVariable Long id) {
        LOG.debug("REST request to getOrderSubjectInfo orderId={}", id);
        SubjectInfo subjectInfo = orderRepository.findOrderSubjectInfo(id)
                .orElseThrow(()-> new EntityNotFoundException(String.format("SubjectInfo not found for orderId = %s", id.toString())));
        return ResponseEntity.ok(subjectInfo);
    }

    @ApiOperation(value = "Информация по заявке", notes = "Информация по заявке", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/{id}/orderinfo", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @OrderReadPermission
    public ResponseEntity<OrderInfo> getOrderInfo(@ApiParam(value = "ID заявки", required = true) @PathVariable Long id) {
        LOG.debug("REST request to getOrderInfo orderId={}", id);
        OrderInfo orderInfo = orderRepository.findOrderInfo(id)
                .orElseThrow(()-> new EntityNotFoundException(String.format("OrderInfo not found for orderId = %s", id.toString())));
        return ResponseEntity.ok(orderInfo);
    }

}
