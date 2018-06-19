package kz.alfabank.alfaordersbpm.domain.models.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.util.Objects;


@ApiModel(description = "Модель документов")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentModel {
    @ApiModelProperty(notes="ID записи") private Long id;
    @ApiModelProperty(notes="Тип документа") private DictionaryModel documentTypeRef;
    @ApiModelProperty(notes="Номер документа") private String documentNumber;
    @ApiModelProperty(notes="Серия документа") private String documentSeries;
    @ApiModelProperty(notes="Орган выдачи документа") private DictionaryModel issueAuthorityRef;
    @ApiModelProperty(notes="Дата выдачи документа")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate issueDate;
    @ApiModelProperty(notes="Окончание срока документа")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate expirationDate;
    @ApiModelProperty(notes="Место рождения клиента") private DictionaryModel countryRef;

    @JsonCreator
    public DocumentModel(@JsonProperty("id") Long id,
                         @JsonProperty("documentTypeRef") DictionaryModel documentTypeRef,
                         @JsonProperty("documentNumber") String documentNumber,
                         @JsonProperty("documentSeries") String documentSeries,
                         @JsonProperty("issueAuthorityRef") DictionaryModel issueAuthorityRef,
                         @JsonProperty("issueDate") LocalDate issueDate,
                         @JsonProperty("expirationDate") LocalDate expirationDate,
                         @JsonProperty("countryRef") DictionaryModel countryRef) {
        this.id = id;
        this.documentTypeRef = documentTypeRef;
        this.documentNumber = documentNumber;
        this.documentSeries = documentSeries;
        this.issueAuthorityRef = issueAuthorityRef;
        this.issueDate = issueDate;
        this.expirationDate = expirationDate;
        this.countryRef = countryRef;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DictionaryModel getDocumentTypeRef() {
        return documentTypeRef;
    }

    public void setDocumentTypeRef(DictionaryModel documentTypeRef) {
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

    public DictionaryModel getIssueAuthorityRef() {
        return issueAuthorityRef;
    }

    public void setIssueAuthorityRef(DictionaryModel issueAuthorityRef) {
        this.issueAuthorityRef = issueAuthorityRef;
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

    public DictionaryModel getCountryRef() {
        return countryRef;
    }

    public void setCountryRef(DictionaryModel countryRef) {
        this.countryRef = countryRef;
    }

    @Override
    public String toString() {
        return "DocumentModel{" +
                "id=" + id +
                ", documentTypeRef=" + documentTypeRef +
                ", documentNumber='" + documentNumber + '\'' +
                ", documentSeries='" + documentSeries + '\'' +
                ", issueAuthorityRef=" + issueAuthorityRef +
                ", issueDate='" + issueDate + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", countryRef=" + countryRef +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentModel that = (DocumentModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(documentTypeRef, that.documentTypeRef) &&
                Objects.equals(documentNumber, that.documentNumber) &&
                Objects.equals(documentSeries, that.documentSeries) &&
                Objects.equals(issueAuthorityRef, that.issueAuthorityRef) &&
                Objects.equals(issueDate, that.issueDate) &&
                Objects.equals(expirationDate, that.expirationDate) &&
                Objects.equals(countryRef, that.countryRef);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, documentTypeRef, documentNumber, documentSeries, issueAuthorityRef, issueDate, expirationDate, countryRef);
    }
}
