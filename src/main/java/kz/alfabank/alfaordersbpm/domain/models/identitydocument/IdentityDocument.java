package kz.alfabank.alfaordersbpm.domain.models.identitydocument;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.audit.AuditInfo;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.order.AbstractOrder;
import org.hibernate.annotations.RowId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@ApiModel(description = "Документ удостоверяющий личность")
@Entity
@Table(name = "order_identitydocument"
        ,uniqueConstraints={@UniqueConstraint(columnNames = {"order_id", "document_type"})
}
)
@RowId("ROWID")
@EntityListeners(AuditingEntityListener.class)
public class IdentityDocument extends AuditInfo {

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_gen")
    @SequenceGenerator(name = "document_gen", sequenceName = "document_seq", allocationSize = 1)
    private Long id;

    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @NotNull(message = "Для телефона обязательно ID заявки")
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id", insertable = false, updatable = false, nullable = false)
    private AbstractOrder order;

    @ApiModelProperty(notes="Тип документа как ENUM", required = true, allowableValues = "PASSPORT, IDCARD")
    @NotNull(message = "Тип документа не может быть пустым")
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 100)
    private IdentityDocumentType identityDocumentType;

    @ApiModelProperty(notes="Ссылка на тип документа oCRM", required = true)
    @Embedded
    @NotNull(message = "Тип документа oCRM не может быть пустым")
    private CommonServiceRef documentTypeRef;

    @ApiModelProperty(notes="Номер документа", required = true)
    @NotBlank(message = "Номер документане может быть пустым")
    @Column(name = "document_number", length = 50, nullable = false)
    private String documentNumber;

    @ApiModelProperty(notes="Серия документа")
    @Column(name = "document_series", length = 50)
    private String documentSeries;

    @ApiModelProperty(notes="Дата выдачи", required = true)
    @NotNull(message = "Дата выдачи не может быть пустой")
    @PastOrPresent(message = "Дата выдачи не может быть больше текущего дня")
    private LocalDate issueDate;

    @ApiModelProperty(notes="Дата окончания", required = true)
    @NotNull(message = "Дата окончания не может быть пустой")
    @Future(message = "Дата окончания должна быть больше текущего дня")
    private LocalDate expirationDate;

    @ApiModelProperty(notes="Орган выдачи (ccсылка oCRM)", required = true)
    @Embedded
    @NotNull(message = "Орган выдачи не может быть пустым")
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "authority_code", nullable = false)),
            @AttributeOverride(name = "text", column = @Column(name = "authority_name", nullable = false)),
            @AttributeOverride(name = "dictName", column = @Column(name = "authority_source", nullable = false)),
            @AttributeOverride(name = "dictLang", column = @Column(name = "authority_name_lang", nullable = false))
    })
    private CommonServiceRef issueAuthorityRef;

    @ApiModelProperty(notes="Страна", required = true)
    @Embedded
    @NotNull(message = "Страна не может быть пустой")
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "country_code", nullable = false)),
            @AttributeOverride(name = "text", column = @Column(name = "country_name", nullable = false)),
            @AttributeOverride(name = "dictName", column = @Column(name = "country_source", nullable = false)),
            @AttributeOverride(name = "dictLang", column = @Column(name = "country_name_lang", nullable = false))
    })
    private CommonServiceRef countryRef;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public AbstractOrder getOrder() {
        return order;
    }

    public void setOrder(AbstractOrder order) {
        this.order = order;
    }

    public IdentityDocumentType getIdentityDocumentType() {
        return identityDocumentType;
    }

    public void setIdentityDocumentType(IdentityDocumentType identityDocumentType) {
        this.identityDocumentType = identityDocumentType;
    }

    public CommonServiceRef getDocumentTypeRef() {
        return documentTypeRef;
    }

    public void setDocumentTypeRef(CommonServiceRef documentTypeRef) {
        this.documentTypeRef = documentTypeRef;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentSeries() {
        return documentSeries;
    }

    public void setDocumentSeries(String documentSeries) {
        this.documentSeries = documentSeries;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public CommonServiceRef getIssueAuthorityRef() {
        return issueAuthorityRef;
    }

    public void setIssueAuthorityRef(CommonServiceRef issueAuthorityRef) {
        this.issueAuthorityRef = issueAuthorityRef;
    }

    public CommonServiceRef getCountryRef() {
        return countryRef;
    }

    public void setCountryRef(CommonServiceRef countryRef) {
        this.countryRef = countryRef;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IdentityDocument{");
        sb.append("id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", identityDocumentType=").append(identityDocumentType);
        sb.append(", documentTypeRef=").append(documentTypeRef);
        sb.append(", documentNumber='").append(documentNumber).append('\'');
        sb.append(", documentSeries='").append(documentSeries).append('\'');
        sb.append(", issueDate=").append(issueDate);
        sb.append(", expirationDate=").append(expirationDate);
        sb.append(", issueAuthorityRef=").append(issueAuthorityRef);
        sb.append(", countryRef=").append(countryRef);
        sb.append('}');
        return sb.toString();
    }
}
