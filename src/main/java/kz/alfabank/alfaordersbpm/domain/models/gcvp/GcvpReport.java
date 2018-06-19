package kz.alfabank.alfaordersbpm.domain.models.gcvp;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class GcvpReport {

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    private final long id;
    @ApiModelProperty(notes="идентификатор записи заявки", required = true)
    private final long orderId;
    @ApiModelProperty(notes="идентификатор запроса гцвп (id)")
    private final long gcvpRequestId;
    @ApiModelProperty(notes="дата создания записи", required = true)
    private final LocalDate createdDate;
    @ApiModelProperty(notes="признак наличия ошибки гцвп", required = true)
    private final boolean hasErrors;
    @ApiModelProperty(notes="идентификатор запроса альфа-кросс")
    private final String requestId;
    @ApiModelProperty(notes="идентификатор отчета альфа-кросс")
    private final String gcvpId;
    @ApiModelProperty(notes="дата создания альфа-кросс")
    private final String gcvpCreateDate;
    @ApiModelProperty(notes="состояние альфа-кросс")
    private final String state;
    @ApiModelProperty(notes="иин субъекта")
    private final String iin;
    @ApiModelProperty(notes="фамилия")
    private final String lastName;
    @ApiModelProperty(notes="имя")
    private final String firstName;
    @ApiModelProperty(notes="отчество")
    private final String middleName;
    @ApiModelProperty(notes="дата рождения")
    private final LocalDate birthDate;
    @ApiModelProperty(notes="статус альфа-кросс")
    private final String status;
    @ApiModelProperty(notes="кол-во взносов")
    private final BigDecimal numberOfTransfers;
    @ApiModelProperty(notes="средний размер взносов")
    private final BigDecimal averageAmountMPV;
    @ApiModelProperty(notes="код ошибки альфа-кросс")
    private final String errorCode;
    @ApiModelProperty(notes="текст ошибки альфа-кросс")
    private final String errorMessage;
    private final String errorServity;

    private GcvpReport(ResultSet resultSet) throws SQLException {
        if (resultSet == null)
            throw new IllegalArgumentException("To create GcvpReport resultSet must not be null");
        this.id = resultSet.getLong(1);
        this.orderId = resultSet.getLong(2);
        this.gcvpRequestId = resultSet.getLong(3);
        this.createdDate = resultSet.getDate(4).toLocalDate();
        this.hasErrors = resultSet.getBoolean(5);
        this.requestId = resultSet.getString(6);
        this.gcvpId = resultSet.getString(7);
        this.gcvpCreateDate = resultSet.getString(8);
        this.state = resultSet.getString(9);
        this.iin = resultSet.getString(10);
        this.lastName = resultSet.getString(11);
        this.firstName = resultSet.getString(12);
        this.middleName = resultSet.getString(13);
        Date theBirthDate = resultSet.getDate(14);
        this.birthDate = theBirthDate==null? null : theBirthDate.toLocalDate();
        this.status = resultSet.getString(15);
        this.numberOfTransfers = resultSet.getBigDecimal(16);
        this.averageAmountMPV = resultSet.getBigDecimal(17);
        this.errorCode = resultSet.getString(18);
        this.errorMessage = resultSet.getString(19);
        this.errorServity = resultSet.getString(20);
    }

    public static GcvpReport of(ResultSet resultSet) throws SQLException {
        return new GcvpReport(resultSet);
    }

    public long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getGcvpRequestId() {
        return gcvpRequestId;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getGcvpId() {
        return gcvpId;
    }

    public String getGcvpCreateDate() {
        return gcvpCreateDate;
    }

    public String getState() {
        return state;
    }

    public String getIin() {
        return iin;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getStatus() {
        return status;
    }

    public String getNumberOfTransfers() {
        return numberOfTransfers==null? null : numberOfTransfers.toPlainString();
    }

    public String getAverageAmountMPV() {
        return averageAmountMPV==null? null : averageAmountMPV.toPlainString();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorServity() {
        return errorServity;
    }
}
