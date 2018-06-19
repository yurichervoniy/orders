package kz.alfabank.alfaordersbpm.domain.models.dto;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PhoneConfirmationResponse {

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    private final Long id;

    @ApiModelProperty(notes="Текущее время", required = true)
    private final LocalDateTime currentDate = LocalDateTime.now();

    @ApiModelProperty(notes="Дата истечения срока действия", required = true)
    private final LocalDateTime expireDate;

    @ApiModelProperty(notes="Счетчик отправлений", required = true)
    private final Integer sendCount;

    @ApiModelProperty(notes="Лимит отправлений", required = true)
    private final Integer sendLimit;

    @ApiModelProperty(notes="Сообщение пользователю", required = true)
    private final String message;

    @ApiModelProperty(notes="Дата ожидания", required = true)
    private final LocalDateTime waitDate;

    public PhoneConfirmationResponse(Long id, LocalDateTime expireDate, Integer sendCount, Integer sendLimit, String message, LocalDateTime waitDate) {
        this.id = id;
        this.expireDate = expireDate;
        this.sendCount = sendCount;
        this.sendLimit = sendLimit;
        this.message = message;
        this.waitDate = waitDate;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCurrentDate() {
        return currentDate;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public Integer getSendLimit() {
        return sendLimit;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getWaitDate() {
        return waitDate;
    }

    public long getWaitSeconds(){
        long seconds = currentDate.until(waitDate, ChronoUnit.SECONDS);
        return seconds < 0 ? 0 : seconds;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PhoneConfirmationResponse{");
        sb.append("id=").append(id);
        sb.append(", currentDate=").append(currentDate);
        sb.append(", expireDate=").append(expireDate);
        sb.append(", sendCount=").append(sendCount);
        sb.append(", sendLimit=").append(sendLimit);
        sb.append(", message='").append(message).append('\'');
        sb.append(", waitDate=").append(waitDate);
        sb.append('}');
        return sb.toString();
    }
}
