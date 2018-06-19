package kz.alfabank.alfaordersbpm.domain.models.blacklist;

import io.swagger.annotations.ApiModelProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class BLCheckResults {
    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    private final long id;
    @ApiModelProperty(notes="идентификатор заявки (id)", required = true)
    private final long orderId;
    @ApiModelProperty(notes="дата создания записи (id)", required = true)
    private final LocalDate createdDate;
    @ApiModelProperty(notes = "Признак проверки на ЧС")
    private final String serviceName;
    @ApiModelProperty(notes = "Статус ответа")
    private final String responseStatus;
    @ApiModelProperty(notes="Признак успешности запроса, ответ получен")
    private final boolean requestIsSuccessful;
    @ApiModelProperty(notes = "Результат проверки")
    private final String responseComment;
    @ApiModelProperty(notes = "Реакция 1")
    private final String reactionOne;
    @ApiModelProperty(notes = "Реакция 2")
    private final String reactionTwo;

    public BLCheckResults(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getLong(1);
        this.orderId = resultSet.getLong(2);
        this.createdDate = resultSet.getDate(3).toLocalDate();
        this.serviceName = resultSet.getString(4);
        this.responseStatus = resultSet.getString(5);
        this.requestIsSuccessful = resultSet.getBoolean(6);
        this.responseComment = resultSet.getString(7);
        this.reactionOne = resultSet.getString(8);
        this.reactionTwo = resultSet.getString(9);
    }

    public static BLCheckResults of(ResultSet resultSet) throws SQLException {
        return new BLCheckResults(resultSet);
    }

    public long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public boolean isRequestIsSuccessful() {
        return requestIsSuccessful;
    }

    public String getResponseComment() {
        return responseComment;
    }

    public String getReactionOne() {
        return reactionOne;
    }

    public String getReactionTwo() {
        return reactionTwo;
    }
}
