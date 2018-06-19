package kz.alfabank.alfaordersbpm.domain.repositories.pkb;

import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.exception.InternalServerException;
import kz.alfabank.alfaordersbpm.domain.models.pkb.PkbChecks;
import kz.alfabank.alfaordersbpm.domain.models.pkb.PkbData;
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
public class PkbRepository {
    private static final Logger LOG = LoggerFactory.getLogger(PkbRepository.class);
    private DataSource dataSource;

    @Autowired
    public PkbRepository(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private enum DBCmd {
        GET_PKB_DATA("BEGIN pkg_pkb_api.get_pkb_data(:p_order_id, :cursor); END;", 20),
        GET_PKB_CHECKS("BEGIN pkg_pkb_api.get_pkb_checks(:p_order_id, :cursor); END;", 20),
        GET_PKB_HTML("BEGIN pkg_pkb_api.get_html(:p_order_id, :cursor); END;", 20),
        GET_PKB_RESPONSE("BEGIN pkg_pkb_api.get_xml(:p_order_id, :cursor); END;", 20);

        private final String text;
        private final int timeOut;

        DBCmd(final String text, final int timeOut) {
            this.text = text;
            this.timeOut = timeOut;
        }

        public int getTimeOut() {
            return timeOut;
        }

        public String getCmd() {
            return text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private static void executeCallableStatement(CallableStatement stm) throws SQLException {
        try {
            stm.execute();
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            if ("72000".equals(e.getSQLState()) && (errorCode == 4068 || errorCode == 4061 || errorCode == 4065 || errorCode == 4067)) {
                LOG.warn(String.format("executeRetry=>%s", Arrays.toString(Thread.currentThread().getStackTrace())), e);
                stm.execute(); //Retry Execution on Recoverable Exceptions
            } else
                throw e;
        }
    }

    public PkbData getPkbMainData(Long orderId) {
        DBCmd cmd = DBCmd.GET_PKB_DATA;

        try(Connection con = dataSource.getConnection();
            CallableStatement st = con.prepareCall(cmd.getCmd())) {
            st.setEscapeProcessing(false);
            st.setQueryTimeout(cmd.getTimeOut());
            st.setLong(1, orderId);
            st.registerOutParameter(2, OracleTypes.CURSOR);
            executeCallableStatement(st);
            try (ResultSet rset = st.getObject(2, ResultSet.class)) {
                PkbData pkbData = new PkbData();
                while (rset.next()) {
                    pkbData.setRequestDate(rset.getString(1));
                    pkbData.setResponseDate(rset.getString(2));
                    pkbData.setRequestBody(rset.getString(3));
                    pkbData.setSoapResult(rset.getString(4));
                    pkbData.setException(rset.getString(5));
                    pkbData.setXmlName(rset.getString(6));
                    pkbData.setHtmlName(rset.getString(7));
                }
                if (pkbData.getRequestDate() == null)
                    throw new EntityNotFoundException("PKB checks not found for ID: "+orderId.toString());

                return pkbData;
            }
        } catch (SQLException e){
            LOG.error(String.format("getPkbMainData: %s", e.getMessage()), e);
            throw new InternalServerException("getPkbMainData: "+e);
        }
    }

    public List<PkbChecks> getPkbChecksData(Long orderId) {
        DBCmd cmd = DBCmd.GET_PKB_CHECKS;

        try(Connection con = dataSource.getConnection();
            CallableStatement st = con.prepareCall(cmd.getCmd())) {
            st.setEscapeProcessing(false);
            st.setQueryTimeout(cmd.getTimeOut());
            st.setLong(1, orderId);
            st.registerOutParameter(2, OracleTypes.CURSOR);
            executeCallableStatement(st);
            try (ResultSet rset = st.getObject(2, ResultSet.class)) {
                List<PkbChecks> pkbChecks = new ArrayList<>();
                PkbChecks pkbCheck;
                while (rset.next()) {
                    pkbCheck = new PkbChecks();
                    pkbCheck.setId(rset.getInt(1));
                    pkbCheck.setCode(rset.getString(2));
                    pkbCheck.setDescription(rset.getString(3));
                    pkbCheck.setValue(rset.getString(4));
                    pkbChecks.add(pkbCheck);
                }
                if (pkbChecks.isEmpty())
                    throw new EntityNotFoundException("PKB checks not found for ID: "+orderId.toString());

                return pkbChecks;
            }
        } catch (SQLException e){
            LOG.error("getPkbChecksData: ",e);
            throw new IllegalArgumentException("getPkbChecksData: "+e);
        }
    }

    public String getPkbHtml(Long orderId) {
        DBCmd cmd = DBCmd.GET_PKB_HTML;
        try(Connection con = dataSource.getConnection();
            CallableStatement st = con.prepareCall(cmd.getCmd())) {
            st.setEscapeProcessing(false);
            st.setQueryTimeout(cmd.getTimeOut());
            st.setLong(1, orderId);
            st.registerOutParameter(2, OracleTypes.CURSOR);
            executeCallableStatement(st);
            try (ResultSet rset = st.getObject(2, ResultSet.class)) {
                String html = null;
                while (rset.next())
                    html = rset.getString(1);

                if (html == null)
                    throw new EntityNotFoundException("HTML report not found for ID: "+orderId.toString());

                return html;
            }
        } catch (SQLException e){
            LOG.error("getPkbHtml: ",e);
            throw new IllegalArgumentException("getPkbHtml: "+e);
        }
    }

    public String getPkbResponse(Long orderId) {
        DBCmd cmd = DBCmd.GET_PKB_RESPONSE;
        try(Connection con = dataSource.getConnection();
            CallableStatement st = con.prepareCall(cmd.getCmd())) {
            st.setEscapeProcessing(false);
            st.setQueryTimeout(cmd.getTimeOut());
            st.setLong(1, orderId);
            st.registerOutParameter(2, OracleTypes.CURSOR);
            executeCallableStatement(st);
            try (ResultSet rset = st.getObject(2, ResultSet.class)) {
                String result = null;
                while (rset.next())
                    result = rset.getString(1);

                if (result == null)
                    throw new EntityNotFoundException("XML report not found for ID: "+orderId.toString());

                return result;
            }
        } catch (SQLException e){
            LOG.error("getPkbResponse: ",e);
            throw new IllegalArgumentException("getPkbResponse: "+e);
        }
    }

}
