package kz.alfabank.alfaordersbpm.domain.repositories.gcvp;

import kz.alfabank.alfaordersbpm.domain.models.gcvp.GcvpReport;
import kz.alfabank.alfaordersbpm.domain.models.gcvp.GcvpReportDetail;
import kz.alfabank.alfaordersbpm.domain.models.gcvp.GcvpRequestResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GcvpReportDao {

    Optional<GcvpReport> getOrderLastReport(long orderId) throws SQLException;

    List<GcvpReportDetail> getReportDetails(long reportId) throws SQLException;

    Optional<GcvpRequestResponse> getGcvpRqRsInfo(long id) throws SQLException;

}
