package kz.alfabank.alfaordersbpm.domain.models.approval;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel(description = "Корректировка полей службами")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalModel {
    @ApiModelProperty(notes="Номер заявки", required = true)
    private Long orderId;

    @ApiModelProperty(notes="Тип службы проверки", required = true)
    private String service;

    @ApiModelProperty(notes="Комментарий службы")
    private String comment;

    @ApiModelProperty(notes="Главный комментарий менеджеру")
    private String generalCommentForManager;

    @ApiModelProperty(notes="Рекомендуемая сумма")
    private BigDecimal recommendedAmount;

    @ApiModelProperty(notes="Возможность менять сумму")
    private Boolean canChangeAmount;

    @ApiModelProperty(notes="Время возврата в очереди")
    private String orderTime;

    @ApiModelProperty(notes="Количество дней отказа")
    private BigDecimal countOfRejectDays;

    @ApiModelProperty(notes="Решение службы", required = true)
    private ServicesModel decision;

    @ApiModelProperty(notes="Дополнительная информация")
    private ServicesModel additionalInfo;

    @ApiModelProperty(notes="Негативная информация")
    private ServicesModel negativeInfo;

    @ApiModelProperty(notes="Проверка по документу")
    private ServicesModel idCardCheck;

    @ApiModelProperty(notes="База данных судебных исполнителей")
    private ServicesModel judgeDBCheck;

    @ApiModelProperty(notes="База данных судебных исполнителей")
    private ServicesModel birthAndResidencePlace;

    @ApiModelProperty(notes="Список полей для корректировки")
    private FieldsModel content;

    @ApiModelProperty(notes="Успешность парсинга ответа")
    private Long isParsed;

    @ApiModelProperty(notes="Статус проверки. 1-закончена.")
    private Long status;

    @ApiModelProperty(notes="Время решения службы")
    private String createdDate;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGeneralCommentForManager() {
        return generalCommentForManager;
    }

    public void setGeneralCommentForManager(String generalCommentForManager) {
        this.generalCommentForManager = generalCommentForManager;
    }

    public BigDecimal getRecommendedAmount() {
        return recommendedAmount;
    }

    public void setRecommendedAmount(BigDecimal recommendedAmount) {
        this.recommendedAmount = recommendedAmount;
    }

    public Boolean getCanChangeAmount() {
        return canChangeAmount;
    }

    public void setCanChangeAmount(Boolean canChangeAmount) {
        this.canChangeAmount = canChangeAmount;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public BigDecimal getCountOfRejectDays() {
        return countOfRejectDays;
    }

    public void setCountOfRejectDays(BigDecimal countOfRejectDays) {
        this.countOfRejectDays = countOfRejectDays;
    }

    public ServicesModel getDecision() {
        return decision;
    }

    public void setDecision(ServicesModel decision) {
        this.decision = decision;
    }

    public ServicesModel getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(ServicesModel additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public ServicesModel getNegativeInfo() {
        return negativeInfo;
    }

    public void setNegativeInfo(ServicesModel negativeInfo) {
        this.negativeInfo = negativeInfo;
    }

    public ServicesModel getIdCardCheck() {
        return idCardCheck;
    }

    public void setIdCardCheck(ServicesModel idCardCheck) {
        this.idCardCheck = idCardCheck;
    }

    public ServicesModel getJudgeDBCheck() {
        return judgeDBCheck;
    }

    public void setJudgeDBCheck(ServicesModel judgeDBCheck) {
        this.judgeDBCheck = judgeDBCheck;
    }

    public ServicesModel getBirthAndResidencePlace() {
        return birthAndResidencePlace;
    }

    public void setBirthAndResidencePlace(ServicesModel birthAndResidencePlace) {
        this.birthAndResidencePlace = birthAndResidencePlace;
    }

    public FieldsModel getContent() {
        return content;
    }

    public void setContent(FieldsModel content) {
        this.content = content;
    }

    public Long getIsParsed() {
        return isParsed;
    }

    public void setIsParsed(Long isParsed) {
        this.isParsed = isParsed;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "ApprovalModel{" +
                "orderId=" + orderId +
                ", service='" + service + '\'' +
                ", comment='" + comment + '\'' +
                ", generalCommentForManager='" + generalCommentForManager + '\'' +
                ", recommendedAmount=" + recommendedAmount +
                ", canChangeAmount=" + canChangeAmount +
                ", orderTime='" + orderTime + '\'' +
                ", countOfRejectDays=" + countOfRejectDays +
                ", decision=" + decision +
                ", additionalInfo=" + additionalInfo +
                ", negativeInfo=" + negativeInfo +
                ", idCardCheck=" + idCardCheck +
                ", judgeDBCheck=" + judgeDBCheck +
                ", birthAndResidencePlace=" + birthAndResidencePlace +
                ", content=" + content +
                ", isParsed=" + isParsed +
                ", status=" + status +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
