package kz.alfabank.alfaordersbpm.domain.repositories.gcvp;

import kz.alfabank.alfaordersbpm.domain.models.gcvp.GcvpReport;
import kz.alfabank.alfaordersbpm.domain.models.gcvp.GcvpReportDetail;
import kz.alfabank.alfaordersbpm.domain.models.gcvp.GcvpRequestResponse;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class GcvpReportDaoImpl implements GcvpReportDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(GcvpReportDaoImpl.class);

    private static final int TIME_OUT = 20;
    private static final String ORDER_LAST_REPORT_CMD = "BEGIN gcvp_orders.getOrderLastReport(:orderId, :cursor); END;";
    private static final String REPORT_DETAILS_CMD = "BEGIN gcvp_orders.getGcvpReportDetails(:reportId, :cursor); END;";
    private static final String REQUEST_RESPONSE_CMD = "BEGIN gcvp_orders.getGcvpRequestResponse(:id, :cursor); END;";

    private final DataSource dataSource;

    @Autowired
    public GcvpReportDaoImpl(@Qualifier("dataSource") final DataSource theDataSource) {
        this.dataSource = theDataSource;
    }

    private static void executeCallableStatement(final CallableStatement stm) throws SQLException {
        try {
            stm.execute();
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            if ("72000".equals(e.getSQLState()) && (errorCode == 4068 || errorCode == 4061 || errorCode == 4065 || errorCode == 4067)) {
                LOGGER.warn(String.format("executeRetry=>%s", Arrays.toString(Thread.currentThread().getStackTrace())), e);
                stm.execute(); //Retry Execution on Recoverable Exceptions
            } else {
                throw e;
            }
        }
    }

    @Override
    public Optional<GcvpReport> getOrderLastReport(final long orderId) throws SQLException {
        LOGGER.debug("getOrderLastReport CMD FOR EXECUTE {}", ORDER_LAST_REPORT_CMD);
        GcvpReport report = null;
        int count = 0;
        try (Connection con = dataSource.getConnection();
             CallableStatement st = con.prepareCall(ORDER_LAST_REPORT_CMD)) {
            st.setEscapeProcessing(false);
            st.setQueryTimeout(TIME_OUT);
            st.setLong(1, orderId);
            st.registerOutParameter(2, OracleTypes.CURSOR);
            executeCallableStatement(st);
            try (ResultSet rs = st.getObject(2, ResultSet.class)) {
               while (rs.next()) {
                   report = GcvpReport.of(rs);
                   if (++count > 1) {
                       throw  new SQLDataException(String.format("Cursor returned more than one GcvpReport on getOrderLastReport with orderId=%s CMD=%s", orderId, ORDER_LAST_REPORT_CMD));
                   }
                }
            }
        } catch (SQLException e) {
            String s = String.format("SQLException in getOrderLastReport. %s", e.toString());
            LOGGER.error(s, e);
            throw e;
        }
        return Optional.ofNullable(report);
    }

    @Override
    public List<GcvpReportDetail> getReportDetails(final long reportId) throws SQLException {
        LOGGER.debug("getReportDetails CMD FOR EXECUTE {}", REPORT_DETAILS_CMD);
        final List<GcvpReportDetail> details = new ArrayList<>(16);
        try (Connection con = dataSource.getConnection();
             CallableStatement st = con.prepareCall(REPORT_DETAILS_CMD)) {
            st.setEscapeProcessing(false);
            st.setQueryTimeout(TIME_OUT);
            st.setLong(1, reportId);
            st.registerOutParameter(2, OracleTypes.CURSOR);
            executeCallableStatement(st);
            try (ResultSet rs = st.getObject(2, ResultSet.class)) {
                while (rs.next()) {
                    details.add(GcvpReportDetail.of(rs));
                }
            }
        } catch (SQLException e) {
            String s = String.format("SQLException in getReportDetails. %s", e.toString());
            LOGGER.error(s, e);
            throw e;
        }
        return details;
    }

    @Override
    public Optional<GcvpRequestResponse> getGcvpRqRsInfo(final long id) throws SQLException {
        LOGGER.debug("getGcvpRqRsInfo CMD FOR EXECUTE {}", REQUEST_RESPONSE_CMD);
        GcvpRequestResponse requestResponse = null;
        int count = 0;
        try (Connection con = dataSource.getConnection();
             CallableStatement st = con.prepareCall(REQUEST_RESPONSE_CMD)) {
            st.setEscapeProcessing(false);
            st.setQueryTimeout(TIME_OUT);
            st.setLong(1, id);
            st.registerOutParameter(2, OracleTypes.CURSOR);
            executeCallableStatement(st);
            try (ResultSet rs = st.getObject(2, ResultSet.class)) {
                while (rs.next()) {
                    requestResponse = GcvpRequestResponse.of(rs);
                    if (++count > 1) {
                        throw  new SQLDataException(String.format("Cursor returned more than one GcvpRequestResponse on getGcvpRqRsInfo with Id=%s CMD=%s", id, REQUEST_RESPONSE_CMD));
                    }
                }
            }
        } catch (SQLException e) {
            String s = String.format("SQLException in getGcvpRqRsInfo. %s", e.toString());
            LOGGER.error(s, e);
            throw e;
        }
        return Optional.ofNullable(requestResponse);
    }
}
