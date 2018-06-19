package kz.alfabank.alfaordersbpm.domain.repositories.blacklist;

import kz.alfabank.alfaordersbpm.domain.models.blacklist.BLCheckResults;
import kz.alfabank.alfaordersbpm.domain.models.blacklist.BLRequestResponse;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class BlackListDAOImpl implements BlackListDAO{

    private static final Logger LOGGER = LoggerFactory.getLogger(BlackListDAOImpl.class);

    private static final int TIME_OUT = 20;
    private static final String REQUEST_RESPONSE_CMD = "BEGIN BLACK_LIST_CHECK.getrequest_response(:in_order_id, :out_result); END;";
    private static final String GETRESULT_CMD = "BEGIN BLACK_LIST_CHECK.getresultchecks(:in_order_id, :out_result); END;";
    private final DataSource dataSource;

    @Autowired
    public BlackListDAOImpl(@Qualifier("dataSource") final DataSource theDataSource) {
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
    public List<BLRequestResponse> getBLRqRsInfo(final long id) throws SQLException {
        LOGGER.debug("getBLRqRsInfo CMD FOR EXECUTE {}", REQUEST_RESPONSE_CMD);
        final List<BLRequestResponse> records = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             CallableStatement st = con.prepareCall(REQUEST_RESPONSE_CMD)) {
            st.setEscapeProcessing(false);
            st.setQueryTimeout(TIME_OUT);
            st.setLong(1, id);
            st.registerOutParameter(2, OracleTypes.CURSOR);
            executeCallableStatement(st);
            try (ResultSet rs = st.getObject(2, ResultSet.class)) {
                while (rs.next()) {
                    records.add(BLRequestResponse.of(rs));
                }
            }
        } catch (SQLException e) {
            String s = String.format("SQLException in getBLRqRsInfo. %s", e.toString());
            LOGGER.error(s, e);
            throw e;
        }
        return records;
    }

    @Override
    public List<BLCheckResults> getResultsChecks(long id) throws SQLException {
        LOGGER.debug("getResultsChecks CMD FOR EXECUTE {}", GETRESULT_CMD);
        final List<BLCheckResults> records = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             CallableStatement st = con.prepareCall(GETRESULT_CMD)) {
            st.setEscapeProcessing(false);
            st.setQueryTimeout(TIME_OUT);
            st.setLong(1, id);
            st.registerOutParameter(2, OracleTypes.CURSOR);
            executeCallableStatement(st);
            try (ResultSet rs = st.getObject(2, ResultSet.class)) {
                while (rs.next()) {
                    records.add(BLCheckResults.of(rs));
                }
            }
        } catch (SQLException e) {
            String s = String.format("SQLException in getResultsChecks. %s", e.toString());
            LOGGER.error(s, e);
            throw e;
        }
        return records;
    }


}
