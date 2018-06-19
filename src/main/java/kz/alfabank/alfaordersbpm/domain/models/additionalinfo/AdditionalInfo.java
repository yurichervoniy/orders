package kz.alfabank.alfaordersbpm.domain.models.additionalinfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.audit.AuditInfo;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.order.AbstractOrder;
import org.hibernate.annotations.RowId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@ApiModel(description = "Документ удостоверяющий личность")
@Entity
@Table(name = "order_additional_info"
        ,uniqueConstraints={@UniqueConstraint(columnNames = {"order_id"})
}
)
@RowId("ROWID")
@EntityListeners(AuditingEntityListener.class)
public class AdditionalInfo extends AuditInfo {

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addinfo_gen")
    @SequenceGenerator(name = "addinfo_gen", sequenceName = "work_seq", allocationSize = 1)
    private Long id;

    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @NotNull(message = "Обязательно ID заявки")
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id", insertable = false, updatable = false, nullable = false)
    private AbstractOrder order;

    @ApiModelProperty(notes="Счета в других банках")
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "other_bank_aacount_code")),
            @AttributeOverride(name = "text", column = @Column(name = "other_bank_aacount_name")),
            @AttributeOverride(name = "dictName", column = @Column(name = "other_bank_aacount_source")),
            @AttributeOverride(name = "dictLang", column = @Column(name = "other_bank_aacount_lang"))
    })
    private CommonServiceRef otherBankAccounts;

    @ApiModelProperty(notes="Уровень образования", required = true)
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "education_code", nullable = false)),
            @AttributeOverride(name = "text", column = @Column(name = "education_name", nullable = false)),
            @AttributeOverride(name = "dictName", column = @Column(name = "education_source", nullable = false)),
            @AttributeOverride(name = "dictLang", column = @Column(name = "education_name_lang", nullable = false))
    })
    private CommonServiceRef educationLevel;

    @ApiModelProperty(notes="Семейное положение", required = true)
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "marriage_status_code", nullable = false)),
            @AttributeOverride(name = "text", column = @Column(name = "marriage_status_name", nullable = false)),
            @AttributeOverride(name = "dictName", column = @Column(name = "marriage_status_source", nullable = false)),
            @AttributeOverride(name = "dictLang", column = @Column(name = "marriage_status_name_lang", nullable = false))
    })
    private CommonServiceRef marriageStatus;

    @ApiModelProperty(notes="Признак причастия к обязательству уплаты налогов в инотранном государстве", required = true)
    @Column(name = "is_tax_resident", nullable = false)
    private Boolean taxResident;

    @ApiModelProperty(notes="Страна для налогового резидента")
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "tax_country_code")),
            @AttributeOverride(name = "text", column = @Column(name = "tax_country_name")),
            @AttributeOverride(name = "dictName", column = @Column(name = "tax_country_source")),
            @AttributeOverride(name = "dictLang", column = @Column(name = "tax_country_lang"))
    })
    private CommonServiceRef taxResidentCountry;

    @ApiModelProperty(notes="Признак наличия иностранного вида на жительство", required = true)
    @Column(name = "is_foreign_residence", nullable = false)
    private Boolean foreignResidence;

    @ApiModelProperty(notes="Страна вида на жительства")
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "residence_country_code")),
            @AttributeOverride(name = "text", column = @Column(name = "residence_country_name")),
            @AttributeOverride(name = "dictName", column = @Column(name = "residence_country_source")),
            @AttributeOverride(name = "dictLang", column = @Column(name = "residence_country_lang"))
    })
    private CommonServiceRef foreignResidenceCountry;

    @ApiModelProperty(notes="Признак пребывания на територии США", required = true)
    @Column(name = "is_usa_residence", nullable = false)
    private Boolean usaResidence;

    @ApiModelProperty(notes="ИНН иностранного гражданина")
    @Column(name="foreign_tax_number", length = 100)
    private String foreignTaxNumber;

    @ApiModelProperty(notes="Кодовое слово")
    @Column(name="secret_word", length = 100)
    private String secretWord;

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

    public CommonServiceRef getOtherBankAccounts() {
        return otherBankAccounts;
    }

    public void setOtherBankAccounts(CommonServiceRef otherBankAccounts) {
        this.otherBankAccounts = otherBankAccounts;
    }

    public CommonServiceRef getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(CommonServiceRef educationLevel) {
        this.educationLevel = educationLevel;
    }

    public CommonServiceRef getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(CommonServiceRef marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    public Boolean getTaxResident() {
        return taxResident;
    }

    public void setTaxResident(Boolean taxResident) {
        this.taxResident = taxResident;
    }

    public CommonServiceRef getTaxResidentCountry() {
        return taxResidentCountry;
    }

    public void setTaxResidentCountry(CommonServiceRef taxResidentCountry) {
        this.taxResidentCountry = taxResidentCountry;
    }

    public Boolean getForeignResidence() {
        return foreignResidence;
    }

    public void setForeignResidence(Boolean foreignResidence) {
        this.foreignResidence = foreignResidence;
    }

    public CommonServiceRef getForeignResidenceCountry() {
        return foreignResidenceCountry;
    }

    public void setForeignResidenceCountry(CommonServiceRef foreignResidenceCountry) {
        this.foreignResidenceCountry = foreignResidenceCountry;
    }

    public Boolean getUsaResidence() {
        return usaResidence;
    }

    public void setUsaResidence(Boolean usaResidence) {
        this.usaResidence = usaResidence;
    }

    public String getForeignTaxNumber() {
        return foreignTaxNumber;
    }

    public void setForeignTaxNumber(String foreignTaxNumber) {
        this.foreignTaxNumber = foreignTaxNumber;
    }

    public String getSecretWord() {
        return secretWord;
    }

    public void setSecretWord(String secretWord) {
        this.secretWord = secretWord;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AdditionalInfo{");
        sb.append("id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", otherBankAccounts=").append(otherBankAccounts);
        sb.append(", educationLevel=").append(educationLevel);
        sb.append(", marriageStatus=").append(marriageStatus);
        sb.append(", taxResident=").append(taxResident);
        sb.append(", taxResidentCountry=").append(taxResidentCountry);
        sb.append(", foreignResidence=").append(foreignResidence);
        sb.append(", foreignResidenceCountry=").append(foreignResidenceCountry);
        sb.append(", usaResidence=").append(usaResidence);
        sb.append(", foreignTaxNumber='").append(foreignTaxNumber).append('\'');
        sb.append(", secretWord='").append(secretWord).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
