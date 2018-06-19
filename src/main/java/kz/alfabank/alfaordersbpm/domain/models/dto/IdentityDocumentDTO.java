package kz.alfabank.alfaordersbpm.domain.models.dto;

import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.OrderBinding;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import javax.validation.constraints.*;
import java.time.LocalDate;

public class IdentityDocumentDTO implements OrderBinding {

    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @NotNull(message = "Для документов удост. личность обязательно ID заявки")
    @Positive
    private Long orderId;

    @ApiModelProperty(notes="Ссылка на тип документа oCRM", required = true)
    @NotNull(message = "Тип документа oCRM не может быть пустым")
    private CommonServiceRef documentTypeRef;

    @ApiModelProperty(notes="Номер документа", required = true)
    @NotBlank(message = "Номер документане может быть пустым")
    private String documentNumber;

    @ApiModelProperty(notes="Серия документа")
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
    @NotNull(message = "Орган выдачи не может быть пустым")
    private CommonServiceRef issueAuthorityRef;

    @ApiModelProperty(notes="Страна", required = true)
    @NotNull(message = "Страна не может быть пустой")
    private CommonServiceRef countryRef;

    @Override
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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
        final StringBuilder sb = new StringBuilder("IdentityDocumentDTO{");
        sb.append("orderId=").append(orderId);
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
