package kz.alfabank.alfaordersbpm.domain.models.phoneconfirm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.Constants;
import kz.alfabank.alfaordersbpm.domain.models.audit.AuditInfo;
import kz.alfabank.alfaordersbpm.domain.models.dto.PhoneConfirmation;
import kz.alfabank.alfaordersbpm.domain.models.order.AbstractOrder;
import org.hibernate.annotations.RowId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_smscode_confirmation"
        ,uniqueConstraints={@UniqueConstraint(columnNames = {"phone","order_id"})
}
)
@RowId("ROWID")
@EntityListeners(AuditingEntityListener.class)
public class SmsCodeConfirmation extends AuditInfo{

    private static final int SEND_LIMIT = 4;
    private static final int EXPIRE_MINUTES = 10;
    private static final int WAIT_SECONDS = 30;

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sms_code_confirm_gen")
    @SequenceGenerator(name = "sms_code_confirm_gen", sequenceName = "sms_code_confirm_seq", allocationSize = 1)
    private Long id;

    @ApiModelProperty(notes="Код подтверждения", required = true)
    @NotBlank(message = "Код подтверждения не может быть пустым")
    @Size(min = 4, max = 20, message = "Длина кода подтверждения не соотвествует установленным ограничениям")
    @Column(name = "confirmation_code", length = 20, nullable = false)
    private String confirmationCode;

    @ApiModelProperty(notes="Дата истечения срока действия", required = true)
    @FutureOrPresent(message = "Дата истечения срока действия должна быть в будущем")
    @Column(name = "expire_date", nullable = false)
    private LocalDateTime expireDate;

    @ApiModelProperty(notes="Дата ожидания перед повторной отправкой", required = true)
    @FutureOrPresent(message = "Дата ожидания должна быть в будущем")
    @Column(name = "wait_Date", nullable = false)
    private LocalDateTime waitDate;

    @ApiModelProperty(notes="Сообщение", required = true)
    @NotBlank(message = "Сообщение не может быть пустым")
    @Column(name = "message", nullable = false)
    private String message;

    @ApiModelProperty(notes="Телефон", required = true)
    @NotBlank(message = "Телефон не может быть пустым")
    @Column(name = "phone", nullable = false)
    @Pattern(regexp = Constants.PHONE_REGEX, message = "Номер телефона не соответсвует regexp-у")
    private String phone;

    @ApiModelProperty(notes="Счетчик отправлений", required = true)
    @PositiveOrZero(message = "Кол-во отправлений должно быть больше либо равно нулю")
    @Column(name = "send_count", nullable = false)
    private Integer sendCount;

    @ApiModelProperty(notes="Лимит отправлений", required = true)
    @PositiveOrZero(message = "Лимит отправлений должен быть больше либо равен нулю")
    @Column(name = "send_limit", nullable = false, updatable = false)
    private Integer sendLimit;

    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id", insertable = false, updatable = false, nullable = false)
    private AbstractOrder order;

    SmsCodeConfirmation(){}

    public static int getExpireMinutes(){
        return EXPIRE_MINUTES;
    }

    public static int getWaitSeconds(){
        return WAIT_SECONDS;
    }

    public static SmsCodeConfirmation of(long orderId, PhoneConfirmation phone){
        if(phone.getMobilePhone() == null || phone.getMobilePhone().isEmpty())
            throw new IllegalArgumentException("To create SmsCodeConfirmation parameter phone must not be blank");

        SmsCodeConfirmation result = new SmsCodeConfirmation();
        long randomNum = generateRandomValue();
        String confirmationCode = String.valueOf(randomNum);
        result.setConfirmationCode(confirmationCode);
        result.setSendLimit(SEND_LIMIT);
        result.setSendCount(0);
        result.setPhone(phone.getMobilePhone());
        result.setOrderId(orderId);
        result.setMessage(String.format("%s Ваш код подтверждения заявки в Альфа-Банке",result.getConfirmationCode()));

        return result;
    }

    @JsonIgnore
    public static long generateRandomValue(){
        return (long) (Math.random() * (9999 - 1000)) + 1000;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public Integer getSendLimit() {
        return sendLimit;
    }

    public void setSendLimit(Integer sendLimit) {
        this.sendLimit = sendLimit;
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

    public LocalDateTime getWaitDate() {
        return waitDate;
    }

    public void setWaitDate(LocalDateTime waitDate) {
        this.waitDate = waitDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SmsCodeConfirmation{");
        sb.append("id=").append(id);
        sb.append(", confirmationCode='").append(confirmationCode).append('\'');
        sb.append(", expireDate=").append(expireDate);
        sb.append(", waitDate=").append(waitDate);
        sb.append(", message='").append(message).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", sendCount=").append(sendCount);
        sb.append(", sendLimit=").append(sendLimit);
        sb.append(", orderId=").append(orderId);
        sb.append(", order=").append(order);
        sb.append('}');
        return sb.toString();
    }
}
