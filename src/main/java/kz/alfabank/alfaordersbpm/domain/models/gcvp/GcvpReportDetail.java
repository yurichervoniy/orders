package kz.alfabank.alfaordersbpm.domain.models.gcvp;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class GcvpReportDetail {

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    private final long id;
    @ApiModelProperty(notes="идентификатор отчета", required = true)
    private final long reportId;
    @ApiModelProperty(notes="дата создания записи", required = true)
    private final LocalDate createdDate;
    @ApiModelProperty(notes="идентификатор записи альфа-кросс")
    private final String detailId;
    @ApiModelProperty(notes="дата взноса")
    private final LocalDate dateOfReceipt;
    @ApiModelProperty(notes="отправитель")
    private final String sender;
    @ApiModelProperty(notes="налоговый код альфа-кросс")
    private final String taxcode;
    @ApiModelProperty(notes="бин отправителя")
    private final String bin;
    @ApiModelProperty(notes="сумма")
    private final BigDecimal deductAmount;

    private GcvpReportDetail(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getLong(1);
        this.reportId = resultSet.getLong(2);
        this.createdDate = resultSet.getDate(3).toLocalDate();
        this.detailId = resultSet.getString(4);
        this.dateOfReceipt = resultSet.getDate(5).toLocalDate();
        this.sender = resultSet.getString(6);
        this.taxcode = resultSet.getString(7);
        this.bin = resultSet.getString(8);
        this.deductAmount = resultSet.getBigDecimal(9);
    }

    public static GcvpReportDetail of(ResultSet resultSet) throws SQLException {
        return new GcvpReportDetail(resultSet);
    }

    public long getId() {
        return id;
    }

    public long getReportId() {
        return reportId;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public String getDetailId() {
        return detailId;
    }

    public LocalDate getDateOfReceipt() {
        return dateOfReceipt;
    }

    public String getSender() {
        return sender;
    }

    public String getTaxcode() {
        return taxcode;
    }

    public String getBin() {
        return bin;
    }

    public String getDeductAmount() {
        return deductAmount == null ? null : deductAmount.toPlainString();
    }
}
