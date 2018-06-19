package kz.alfabank.alfaordersbpm.domain.models.approval;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiModel(description = "Поля предназначенные для корректировки")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldsModel {

    @ApiModelProperty(notes="Документ клиента")
    private DocumentModel document;

    @ApiModelProperty(notes="Адрес регистрации клиента")
    private AddressModel registrationAddress;

    @ApiModelProperty(notes="Адрес проживания клиента")
    private AddressModel residenceAddress;

    @ApiModelProperty(notes="Мобильный телефон клиента")
    private PhoneModel mobilePhone;

    @ApiModelProperty(notes="Домашний телефон клиента")
    private PhoneModel homePhone;

    @ApiModelProperty(notes="Рабочий телефон клиента")
    private PhoneModel workPhone;

    @ApiModelProperty(notes="Фото клиента")
    private String clientPhoto;

    @ApiModelProperty(notes="Подписание согласия ПКБ и ГЦВП")
    private String signedGCVPandPkb;

    @ApiModelProperty(notes="Фото документа (Лицевая сторона)")
    private String idCardPhotoFront;

    @ApiModelProperty(notes="Фото документа (Обратная сторона)")
    private String idCardPhotoBack;

    @ApiModelProperty(notes="Основные данные клиента")
    private OrderModel order;

    @ApiModelProperty(notes="Место работы клиента")
    private WorkModel work;

    @ApiModelProperty(notes="Заработная плата клиента")
    private AddressModel workAddress;

    @ApiModelProperty(notes="Тип документа клиента")
    private List<ContactPersonsModel> contactPersons;

    @JsonCreator
    public FieldsModel(@JsonProperty("document") DocumentModel document,
                       @JsonProperty("registrationAddress") AddressModel registrationAddress,
                       @JsonProperty("residenceAddress") AddressModel residenceAddress,
                       @JsonProperty("mobilePhone") PhoneModel mobilePhone,
                       @JsonProperty("homePhone") PhoneModel homePhone,
                       @JsonProperty("workPhone") PhoneModel workPhone,
                       @JsonProperty("clientPhoto") String clientPhoto,
                       @JsonProperty("signedGCVPandPkb") String signedGCVPandPkb,
                       @JsonProperty("idCardPhotoFront") String idCardPhotoFront,
                       @JsonProperty("idCardPhotoBack") String idCardPhotoBack,
                       @JsonProperty("order") OrderModel order,
                       @JsonProperty("work") WorkModel work,
                       @JsonProperty("workAddress") AddressModel workAddress,
                       @JsonProperty("contactPersons") List<ContactPersonsModel> contactPersons) {
        this.document = document;
        this.registrationAddress = registrationAddress;
        this.residenceAddress = residenceAddress;
        this.mobilePhone = mobilePhone;
        this.homePhone = homePhone;
        this.workPhone = workPhone;
        this.clientPhoto = clientPhoto;
        this.signedGCVPandPkb = signedGCVPandPkb;
        this.idCardPhotoFront = idCardPhotoFront;
        this.idCardPhotoBack = idCardPhotoBack;
        this.order = order;
        this.work = work;
        this.workAddress = workAddress;
        this.contactPersons = contactPersons;
    }

    public DocumentModel getDocument() {
        return document;
    }

    public void setDocument(DocumentModel document) {
        this.document = document;
    }

    public AddressModel getRegistrationAddress() {
        return registrationAddress;
    }

    public void setRegistrationAddress(AddressModel registrationAddress) {
        this.registrationAddress = registrationAddress;
    }

    public AddressModel getResidenceAddress() {
        return residenceAddress;
    }

    public void setResidenceAddress(AddressModel residenceAddress) {
        this.residenceAddress = residenceAddress;
    }

    public PhoneModel getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(PhoneModel mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public PhoneModel getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(PhoneModel homePhone) {
        this.homePhone = homePhone;
    }

    public PhoneModel getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(PhoneModel workPhone) {
        this.workPhone = workPhone;
    }

    public String getClientPhoto() {
        return clientPhoto;
    }

    public void setClientPhoto(String clientPhoto) {
        this.clientPhoto = clientPhoto;
    }

    public String getSignedGCVPandPkb() {
        return signedGCVPandPkb;
    }

    public void setSignedGCVPandPkb(String signedGCVPandPkb) {
        this.signedGCVPandPkb = signedGCVPandPkb;
    }

    public String getIdCardPhotoFront() {
        return idCardPhotoFront;
    }

    public void setIdCardPhotoFront(String idCardPhotoFront) {
        this.idCardPhotoFront = idCardPhotoFront;
    }

    public String getIdCardPhotoBack() {
        return idCardPhotoBack;
    }

    public void setIdCardPhotoBack(String idCardPhotoBack) {
        this.idCardPhotoBack = idCardPhotoBack;
    }

    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }

    public WorkModel getWork() {
        return work;
    }

    public void setWork(WorkModel work) {
        this.work = work;
    }

    public AddressModel getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(AddressModel workAddress) {
        this.workAddress = workAddress;
    }

    public List<ContactPersonsModel> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<ContactPersonsModel> contactPersons) {
        this.contactPersons = contactPersons;
    }

    @Override
    public String toString() {
        return "FieldsModel{" +
                "document=" + document +
                ", registrationAddress=" + registrationAddress +
                ", residenceAddress=" + residenceAddress +
                ", mobilePhone=" + mobilePhone +
                ", homePhone=" + homePhone +
                ", workPhone=" + workPhone +
                ", clientPhoto='" + clientPhoto + '\'' +
                ", signedGCVPandPkb='" + signedGCVPandPkb + '\'' +
                ", idCardPhotoFront='" + idCardPhotoFront + '\'' +
                ", idCardPhotoBack='" + idCardPhotoBack + '\'' +
                ", order=" + order +
                ", work=" + work +
                ", workAddress=" + workAddress +
                ", contactPersons=" + contactPersons +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldsModel that = (FieldsModel) o;
        return Objects.equals(document, that.document) &&
                Objects.equals(registrationAddress, that.registrationAddress) &&
                Objects.equals(residenceAddress, that.residenceAddress) &&
                Objects.equals(mobilePhone, that.mobilePhone) &&
                Objects.equals(homePhone, that.homePhone) &&
                Objects.equals(workPhone, that.workPhone) &&
                Objects.equals(order, that.order) &&
                Objects.equals(work, that.work) &&
                Objects.equals(workAddress, that.workAddress) &&
                Objects.equals(contactPersons, that.contactPersons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(document, registrationAddress, residenceAddress, mobilePhone, homePhone, workPhone, order, work, workAddress, contactPersons);
    }

    public boolean checkModifiedFields(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return  false;

        FieldsModel that = (FieldsModel) o;

        return Objects.equals(document, that.document) ||
                Objects.equals(registrationAddress, that.registrationAddress) ||
                Objects.equals(residenceAddress, that.residenceAddress) ||
                Objects.equals(mobilePhone, that.mobilePhone) ||
                Objects.equals(homePhone, that.homePhone) ||
                Objects.equals(workPhone, that.workPhone) ||
                Objects.equals(order, that.order) ||
                Objects.equals(work, that.work) ||
                Objects.equals(workAddress, that.workAddress) ||
                Objects.equals(contactPersons, that.contactPersons);
    }

    public List<CorrectionModel> compareFields(FieldsModel object) throws IllegalAccessException {
        List<CorrectionModel> resultList = new ArrayList<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.get(this) != null && !field.get(this).equals(field.get(object))) {
                CorrectionModel correctionModel = new CorrectionModel();

                correctionModel.setFieldName(field.getName());
                correctionModel.setOldValue(field.get(this));
                correctionModel.setNewValue(field.get(object));
                resultList.add(correctionModel);
            }
        }
        return resultList;
    }
}
