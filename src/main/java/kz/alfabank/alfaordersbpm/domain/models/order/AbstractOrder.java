package kz.alfabank.alfaordersbpm.domain.models.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.entitylisteners.OrderEntityListener;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import org.hibernate.annotations.RowId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/*
    Базовый класс для беззалогового кредитования
    1) Продукт
    2) Страховка (может быть null код, ставка, сумма не null)
    3) Тип платежа
    4) Срок кредита (мб отдельный класс с видом измерения 9 month or 180 days)
    5) запрошенная сумма
    6) Ежемесячный платеж
    7) Дополнительный доход BigDecimal
    8) Первоначальный взнос BigDecimal
    10) Ставка по кредиту BigDecimal
 */
@Entity
@Table(name = "orders")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "order_type")
@RowId("ROWID")
@EntityListeners({AuditingEntityListener.class, OrderEntityListener.class})
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "createProcAppRequest",
                procedureName = "retail_orders.createProcAppRequest",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_orderid", type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_instanceid", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_createdby", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_step", type = String.class)
                })
})
public abstract class AbstractOrder implements Persistable<Long> {

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_gen")
    @SequenceGenerator(name = "orders_gen", sequenceName = "orders_seq", allocationSize = 1)
    private Long id;

    @ApiModelProperty(notes="Номер заяки", required = true)
    @NotBlank(message = "Номер заявки не может быть пустым")
    @Size(min = 1, max = 100, message = "Длина orderNumber не соотвествует установленным ограничениям")
    @Column(name = "order_number", length = 100, nullable = false, unique = true, updatable = false)
    private String orderNumber;

    @ApiModelProperty(notes="Дата создания записи", required = true)
    @NotNull(message = "Дата создания не может быть пустой")
    @PastOrPresent(message = "Дата создания не может быть в будущем")
    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @ApiModelProperty(notes="Кем создана запись", required = true)
    @NotBlank(message = "Создатель не может быть пустым")
    @Size(min = 1, max = 200, message = "Длина createdBy не соотвествует установленным ограничениям")
    @Column(name = "created_by", length = 200, nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @ApiModelProperty(notes="Дата обновления записи")
    @Column(name = "modified_date")
    @PastOrPresent(message = "updated не может быть в будущем")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @ApiModelProperty(notes="Кем обновлена запись")
    @Size(min = 1, max = 200, message = "Длина modifiedBy не соотвествует установленным ограничениям")
    @Column(name = "modified_by",length = 200)
    @LastModifiedBy
    private String modifiedBy;

    @ApiModelProperty(notes="Дата заявки", required = true)
    @NotNull(message = "Дата заявки не может быть пустой")
    @PastOrPresent(message = "Дата заявки(orderDate) не может быть в будущем")
    @Column(name = "order_date", nullable = false, updatable = false)
    private LocalDate orderDate;

    @ApiModelProperty(notes="Дата выдачи")
    //@FutureOrPresent(message = "Дата выдачи(startDate) не может быть в прошлом")
    @Column(name = "start_date")
    private LocalDate startDate;

    @ApiModelProperty(notes="Кредитный продукт", required = true)
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "credit_product_code", nullable = false)),
            @AttributeOverride(name = "text", column = @Column(name = "credit_product_name", nullable = false)),
            @AttributeOverride(name = "dictName", column = @Column(name = "credit_product_source", nullable = false)),
            @AttributeOverride(name = "dictLang", column = @Column(name = "credit_product_name_lang", nullable = false))
    })
    private CommonServiceRef creditProductRef;

    @ApiModelProperty(notes="Процентная ставка", required = true)
    @NotNull(message = "Ставка не может быть пустой")
    @Min(value = 0, message = "Ставка дожна быть больше либо равна нулю")
    @Column(name = "rate", nullable = false, precision = 19, scale = 4)
    private BigDecimal rate;

    @ApiModelProperty(notes="Запрошенная сумма", required = true)
    @NotNull(message = "Запрошенная сумма не может быть пустой")
    @Min(value = 0, message = "Запрошенная сумма дожна быть больше либо равна нулю")
    @Column(name = "requested_amount", nullable = false)
    private BigDecimal requestedAmount;

    @ApiModelProperty(notes="Тип платежа", required = true)
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "payment_type_code", nullable = false)),
            @AttributeOverride(name = "text", column = @Column(name = "payment_type_name", nullable = false)),
            @AttributeOverride(name = "dictName", column = @Column(name = "payment_type_source", nullable = false)),
            @AttributeOverride(name = "dictLang", column = @Column(name = "payment_type_name_lang", nullable = false))
    })
    private CommonServiceRef paymentTypeRef;

    @ApiModelProperty(notes="Тип платежа как ENUM", required = true, allowableValues = "ANNUITY, DIFFERENTIAL")
    @NotNull(message = "Тип платежа не может быть пустым")
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @ApiModelProperty(notes="Ссылка на страховку")
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "insurance_code", nullable = false)),
            @AttributeOverride(name = "text", column = @Column(name = "insurance_name", nullable = false)),
            @AttributeOverride(name = "dictName", column = @Column(name = "insurance_source", nullable = false)),
            @AttributeOverride(name = "dictLang", column = @Column(name = "insurance_name_lang", nullable = false))
    })
    private CommonServiceRef insuranceRef;

    @ApiModelProperty(notes="Сумма страховки")
    @NotNull(message = "Сумма страховки не может быть пустой")
    @Min(value = 0, message = "Сумма страховки дожна быть больше либо равна нулю")
    @Column(name = "insurance_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal insuranceAmount;

    @ApiModelProperty(notes="Регулярный платеж", required = true)
    @NotNull(message = "Регулярный платеж не может быть пустым")
    @Min(value = 0, message = "Регулярный платеж дожен быть больше либо равен нулю")
    @Column(name = "regular_payment", nullable = false)
    private BigDecimal regularPayment;

    @ApiModelProperty(notes="Запрашиваемый срок кредита", required = true)
    @NotNull(message = "Запрашиваемый срок кредита не может быть пустым")
    @Min(value = 1, message = "Запрашиваемый срок должен быть больше либо равен еденице")
    @Column(name = "loan_duration",nullable = false)
    private Integer loanDuration;

    @ApiModelProperty(notes="Еденица измерения периода", required = true, allowableValues = "MONTH")
    @NotNull(message = "Еденица измерения периода не может быть пустой")
    @Enumerated(EnumType.STRING)
    @Column(name = "duration_period", nullable = false)
    private LoanPeriod loanPeriod;

    @ApiModelProperty(notes="Дополнительный доход", required = true)
    @NotNull(message = "Дополнительный доход не может быть пустым")
    @Min(value = 0, message = "Дополнительный доход дожен быть больше либо равен нулю")
    @Column(name = "additional_income", nullable = false)
    private BigDecimal additionalIncome;

    @ApiModelProperty(notes="Первоначальный взнос", required = true)
    @NotNull(message = "Первоначальный взнос не может быть пустым")
    @Min(value = 0, message = "Первоначальный взнос дожен быть больше либо равен нулю")
    @Column(name = "initial_payment", nullable = false)
    private BigDecimal initialPayment;

    @ApiModelProperty(notes="ИИН субъекта", required = true)
    @NotBlank(message = "ИИН не может быть пустым")
    @Size(min = 12, max = 12, message = "Длина ИИН должна быть 12 символов")
    @Column(name="iin", length = 12, nullable = false)
    private String iin;

    @ApiModelProperty(notes="Фамилия субъекта")
    @Size(max = 100, message = "Длина Фамилии не должна первышать 100 символов") // Для оралманов 0
    @Column(name="last_name", length = 100)
    private String lastName;

    @ApiModelProperty(notes="Имя субъекта")
    @Size(max = 100, message = "Длина имени не должна первышать 100 символов")
    @Column(name="first_name", length = 100)
    private String firstName;

    @ApiModelProperty(notes="Отчество субъекта")
    @Size(max = 100, message = "Длина отчества не должна первышать 100 символов")
    @Column(name="middle_name", length = 100)
    private String middleName;

    @ApiModelProperty(notes="Дата рождения субъекта", required = true)
    @NotNull(message = "Дата рождения не может быть пустой")
    @Past(message = "Дата рождения должна быть меньше текущего дня")
    @Column(name="birth_date", nullable = false)
    private LocalDate birthDate;

    @ApiModelProperty(notes="Ссылка на пол")
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "gender_code")),
            @AttributeOverride(name = "text", column = @Column(name = "gender_name")),
            @AttributeOverride(name = "dictName", column = @Column(name = "gender_source")),
            @AttributeOverride(name = "dictLang", column = @Column(name = "gender_lang"))
    })
    private CommonServiceRef genderRef;

    @ApiModelProperty(notes="Пол субъекта как ENUM", required = true, allowableValues = "MALE, FEMALE")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Пол не может быть пустым")
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @ApiModelProperty(notes="Признак активной заявки", required = true)
    @Column(name = "is_active", nullable = false)
    private Boolean active;

    @ApiModelProperty(notes="Признак возможности отмены", required = true)
    @Column(name = "is_cancelable", nullable = false)
    private Boolean cancelable;

    @ApiModelProperty(notes="Сумма переплаты")
    @Column(name = "over_payment")
    private BigDecimal overPayment;

    @ApiModelProperty(notes="Признак запущенной заявки", required = true)
    @Column(name = "IS_STARTED", nullable = false)
    private Boolean started;

    @ApiModelProperty(notes="Состояние заявки", required = true, allowableValues = "CREATED, RUNNING, CANCELED, REFUSED, COMPLETED, FAULTED")
    @NotNull(message = "Состояние заявки не мужет быть пустым")
    @Enumerated(EnumType.STRING)
    @Column(name = "order_state", nullable = false, length = 50)
    private OrderState orderState;

    @ApiModelProperty(notes="ИД Корреляции", required = true)
    @NotBlank(message = "ИД Корреляции не может быть пустым")
    @Size(min = 1, max = 200, message = "Длина correlationId не соотвествует установленным ограничениям")
    @Column(name = "correlation_id", length = 200, nullable = false, unique = true, updatable = false)
    private String correlationId;

    @ApiModelProperty(notes="ИД процесса BPM")
    @Column(name="process_id", length = 100)
    private String piid;

    @ApiModelProperty(notes="ИД запроса в схеме PROCAPP")
    @Column(name="request_id", unique = true)
    private Long requestId;

    @ApiModelProperty(notes="Отображаемый шаг UI")
    @NotBlank(message = "stepUI is blank")
    @Size(min = 1, max = 255, message = "Длина stepUI не соотвествует установленным ограничениям")
    @Column(name = "ui_step", nullable = false)
    private String stepUI;

    @ApiModelProperty(notes="Ответсвенный менеджер", required = true)
    @NotBlank(message = "Ответсвенный менеджер")
    @Size(min = 1, max = 255, message = "Длина assignedTo не соотвествует установленным ограничениям")
    @Column(name = "assigned_to", nullable = false)
    private String assignedTo;

    @JsonIgnore
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "purpose_code")),
            @AttributeOverride(name = "text", column = @Column(name = "purpose_name")),
            @AttributeOverride(name = "dictName", column = @Column(name = "purpose_source")),
            @AttributeOverride(name = "dictLang", column = @Column(name = "purpose_name_lang"))
    })
    private CommonServiceRef creditPurposeRef;

    @JsonIgnore
    @Column(name = "special_mark", nullable = false)
    private Integer specialMark;

    @ApiModelProperty(notes="Код подразделения")
    @NotBlank(message = "Код подразделения не может быть пустым")
    @Column(name = "org_code", nullable = false)
    private String orgCode;

    @ApiModelProperty(notes="Наименование подразделения")
    @NotBlank(message = "Наименование подразделения не может быть пустым")
    @Column(name = "org_name", nullable = false, length = 4000)
    private String orgName;

    @ApiModelProperty(notes="Код продукта Experian")
    @NotBlank(message = "Код продукта Experian не может быть пустым")
    @Column(name = "experian_code", nullable = false, length = 1000)
    private String experianCode;

    @ApiModelProperty(notes="Код продукта Colvir")
    @NotBlank(message = "Код продукта Colvir не может быть пустым")
    @Column(name = "colvir_code", nullable = false, length = 1000)
    private String colvirCode;

    @JsonIgnore
    @ElementCollection
    @MapKeyColumn(name="ATT_NAME")
    @Column(name="ATT_VALUE")
    @CollectionTable(name="order_product_attributes", joinColumns=@JoinColumn(name="order_id"))
    Map<String, String> productAttributes;

    @Override
    public Long getId() {
        return id;
    }

    @JsonIgnore
    @Override
    public boolean isNew() {
        return getId() == null;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public CommonServiceRef getCreditProductRef() {
        return creditProductRef;
    }

    public void setCreditProductRef(CommonServiceRef creditProductRef) {
        this.creditProductRef = creditProductRef;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public CommonServiceRef getPaymentTypeRef() {
        return paymentTypeRef;
    }

    public void setPaymentTypeRef(CommonServiceRef paymentTypeRef) {
        this.paymentTypeRef = paymentTypeRef;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public CommonServiceRef getInsuranceRef() {
        return insuranceRef;
    }

    public void setInsuranceRef(CommonServiceRef insuranceRef) {
        this.insuranceRef = insuranceRef;
    }

    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(BigDecimal insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public BigDecimal getRegularPayment() {
        return regularPayment;
    }

    public void setRegularPayment(BigDecimal regularPayment) {
        this.regularPayment = regularPayment;
    }

    public Integer getLoanDuration() {
        return loanDuration;
    }

    public void setLoanDuration(Integer loanDuration) {
        this.loanDuration = loanDuration;
    }

    public LoanPeriod getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(LoanPeriod loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public BigDecimal getAdditionalIncome() {
        return additionalIncome;
    }

    public void setAdditionalIncome(BigDecimal additionalIncome) {
        this.additionalIncome = additionalIncome;
    }

    public BigDecimal getInitialPayment() {
        return initialPayment;
    }

    public void setInitialPayment(BigDecimal initialPayment) {
        this.initialPayment = initialPayment;
    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public Optional<String> getFirstName() {
        return Optional.ofNullable(firstName);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Optional<String> getLastName() {
        return Optional.ofNullable(lastName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Optional<String> getMiddleName() {
        return Optional.ofNullable(middleName);
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public CommonServiceRef getGenderRef() {
        return genderRef;
    }

    public void setGenderRef(CommonServiceRef genderRef) {
        this.genderRef = genderRef;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(Boolean cancelable) {
        this.cancelable = cancelable;
    }

    public BigDecimal getOverPayment() {
        return overPayment;
    }

    public void setOverPayment(BigDecimal overPayment) {
        this.overPayment = overPayment;
    }

    public Boolean getStarted() {
        return started;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getPiid() {
        return piid;
    }

    public void setPiid(String piid) {
        this.piid = piid;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getStepUI() {
        return stepUI;
    }

    public void setStepUI(String stepUI) {
        this.stepUI = stepUI;
    }

    public CommonServiceRef getCreditPurposeRef() {
        return creditPurposeRef;
    }

    public void setCreditPurposeRef(CommonServiceRef creditPurposeRef) {
        this.creditPurposeRef = creditPurposeRef;
    }

    public Integer getSpecialMark() {
        return specialMark;
    }

    public void setSpecialMark(Integer specialMark) {
        this.specialMark = specialMark;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Map<String, String> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(Map<String, String> productAttributes) {
        this.productAttributes = productAttributes;
    }

    public String getExperianCode() {
        return experianCode;
    }

    public void setExperianCode(String experianCode) {
        this.experianCode = experianCode;
    }

    public String getColvirCode() {
        return colvirCode;
    }

    public void setColvirCode(String colvirCode) {
        this.colvirCode = colvirCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AbstractOrder{");
        sb.append("id=").append(id);
        sb.append(", orderNumber='").append(orderNumber).append('\'');
        sb.append(", createdDate=").append(createdDate);
        sb.append(", createdBy='").append(createdBy).append('\'');
        sb.append(", modifiedDate=").append(modifiedDate);
        sb.append(", modifiedBy='").append(modifiedBy).append('\'');
        sb.append(", orderDate=").append(orderDate);
        sb.append(", startDate=").append(startDate);
        sb.append(", creditProductRef=").append(creditProductRef);
        sb.append(", rate=").append(rate);
        sb.append(", requestedAmount=").append(requestedAmount);
        sb.append(", paymentTypeRef=").append(paymentTypeRef);
        sb.append(", paymentType=").append(paymentType);
        sb.append(", insuranceRef=").append(insuranceRef);
        sb.append(", insuranceAmount=").append(insuranceAmount);
        sb.append(", regularPayment=").append(regularPayment);
        sb.append(", loanDuration=").append(loanDuration);
        sb.append(", loanPeriod=").append(loanPeriod);
        sb.append(", additionalIncome=").append(additionalIncome);
        sb.append(", initialPayment=").append(initialPayment);
        sb.append(", iin='").append(iin).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", middleName='").append(middleName).append('\'');
        sb.append(", birthDate=").append(birthDate);
        sb.append(", genderRef=").append(genderRef);
        sb.append(", gender=").append(gender);
        sb.append(", active=").append(active);
        sb.append(", cancelable=").append(cancelable);
        sb.append(", overPayment=").append(overPayment);
        sb.append(", started=").append(started);
        sb.append(", orderState=").append(orderState);
        sb.append(", correlationId='").append(correlationId).append('\'');
        sb.append(", piid='").append(piid).append('\'');
        sb.append(", requestId=").append(requestId);
        sb.append(", stepUI='").append(stepUI).append('\'');
        sb.append(", assignedTo='").append(assignedTo).append('\'');
        sb.append(", creditPurposeRef=").append(creditPurposeRef);
        sb.append(", specialMark=").append(specialMark);
        sb.append(", orgCode='").append(orgCode).append('\'');
        sb.append(", orgName='").append(orgName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
