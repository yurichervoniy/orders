package kz.alfabank.alfaordersbpm.domain.service.order;

import kz.alfabank.alfaordersbpm.util.JdbcUtil;
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
import java.util.*;

@Repository
public class RetailOrderPropertiesImpl implements RetailOrderProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetailOrderPropertiesImpl.class);

    private final DataSource dataSource;
    private final String sqlCmd;
    private final String sqlCmdForServices;
    private final int timeOut;

    @Autowired
    public RetailOrderPropertiesImpl(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
        this.sqlCmd = "BEGIN retail_orders.getOrderProperties(:orderId, :cursor); END;";
        this.timeOut = 30;
        this.sqlCmdForServices = "BEGIN pkg_order_service_info.getOrderInfoForServices(:orderId, :cursor); END;";
    }

    @Override
    public Map<String, String> getOrderProperties(long orderId) throws SQLException {
        LOGGER.debug("getOrderProperties CMD FOR EXECUTE {}", sqlCmd);
        final Map<String, String> result = new HashMap<>(100);
        try (Connection con = dataSource.getConnection();
             CallableStatement st = con.prepareCall(sqlCmd)) {
            st.setEscapeProcessing(false);
            st.setQueryTimeout(timeOut);
            st.setLong(1, orderId);
            st.registerOutParameter(2, OracleTypes.CURSOR);
            JdbcUtil.executeCallableStatement(st, LOGGER);
            try (ResultSet rs = st.getObject(2, ResultSet.class)) {
                rs.setFetchSize(50);
                while (rs.next()) {
                    result.put(rs.getString(1), rs.getString(2));
                }
            }
        } catch (SQLException e) {
            String s = String.format("SQLException in getOrderProperties. %s", e.toString());
            LOGGER.error(s, e);
            throw e;
        }
        return result;
    }

    @Override
    public Map<String, String> getOrderServiceInfo(Long orderId) throws SQLException {
        LOGGER.debug("getOrderServiceInfo CMD FOR EXECUTE {}", sqlCmdForServices);
        final Map<String, String> result = new HashMap<>(100);
        try (Connection con = dataSource.getConnection();
             CallableStatement st = con.prepareCall(sqlCmdForServices)) {
            st.setEscapeProcessing(false);
            st.setQueryTimeout(timeOut);
            st.setLong(1, orderId);
            st.registerOutParameter(2, OracleTypes.CURSOR);
            JdbcUtil.executeCallableStatement(st, LOGGER);
            try (ResultSet rs = st.getObject(2, ResultSet.class)) {
                rs.setFetchSize(50);
                while (rs.next()) {
                    result.put(rs.getString(1), rs.getString(2));
                }
            }
        } catch (SQLException e) {
            String s = String.format("SQLException in getOrderServiceInfo. %s", e.toString());
            LOGGER.error(s, e);
            throw e;
        }
        return result;
    }


}
