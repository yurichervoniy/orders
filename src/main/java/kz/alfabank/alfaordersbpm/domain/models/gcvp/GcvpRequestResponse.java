package kz.alfabank.alfaordersbpm.domain.models.gcvp;


import io.swagger.annotations.ApiModelProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class GcvpRequestResponse {

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    private final long id;
    @ApiModelProperty(notes="идентификатор заявки (id)", required = true)
    private final long orderId;
    @ApiModelProperty(notes="дата создания записи (id)", required = true)
    private final LocalDate createdDate;
    @ApiModelProperty(notes="Тело запроса SOAP")
    private final String requestBody;
    @ApiModelProperty(notes="Признак валидности запроса")
    private final boolean requestIsValid;
    @ApiModelProperty(notes="Тело ответа SOAP")
    private final String responseBody;
    @ApiModelProperty(notes="Признак успешности запроса, ответ получен")
    private final boolean requestIsSuccessful;
    @ApiModelProperty(notes="Http статус ответа")
    private final String httpStatus;
    @ApiModelProperty(notes="Признак успеха при парсинге ответа")
    private final boolean reportParsed;
    @ApiModelProperty(notes="Текст ошибки при парсинге ответа")
    private final String reportErrorMessage;

    private GcvpRequestResponse(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getLong(1);
        this.orderId = resultSet.getLong(2);
        this.createdDate = resultSet.getDate(3).toLocalDate();
        this.requestBody = resultSet.getString(4);
        this.requestIsValid = resultSet.getBoolean(5);
        this.responseBody = resultSet.getString(6);
        this.requestIsSuccessful = resultSet.getBoolean(7);
        this.httpStatus = resultSet.getString(8);
        this.reportParsed = resultSet.getBoolean(9);
        this.reportErrorMessage = resultSet.getString(10);
    }

    public static GcvpRequestResponse of(ResultSet resultSet) throws SQLException {
        return new GcvpRequestResponse(resultSet);
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

    public String getRequestBody() {
        return requestBody;
    }

    public boolean isRequestIsValid() {
        return requestIsValid;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public boolean isRequestIsSuccessful() {
        return requestIsSuccessful;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public boolean isReportParsed() {
        return reportParsed;
    }

    public String getReportErrorMessage() {
        return reportErrorMessage;
    }
}
