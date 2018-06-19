package kz.alfabank.alfaordersbpm.domain.service.movement;

import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.movement.OrderMovement;
import kz.alfabank.alfaordersbpm.util.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import oracle.jdbc.OracleTypes;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

@Repository
public class OrderMovementServiceImpl implements OrderMovementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderMovementServiceImpl.class);

    private final DataSource dataSource;
    private final String sqlCmd;
    private final int timeOut;

    @Autowired
    public OrderMovementServiceImpl(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
        this.sqlCmd = "BEGIN PKG_ORDERS_MOVEMENT.get_order_movement(:orderId, :cursor); END;";
        this.timeOut = 30;
    }

    @Override
    public List<OrderMovement> getByOrderId(Long orderId) throws SQLException {
        LOGGER.debug("getByOrderId CMD FOR EXECUTE {}", sqlCmd);
        List<OrderMovement> orderMovements = new ArrayList<>();
        OrderMovement orderMovement;
        try (Connection con = dataSource.getConnection();
             CallableStatement st = con.prepareCall(sqlCmd)) {
            st.setEscapeProcessing(false);
            st.setQueryTimeout(timeOut);
            st.setLong(1, orderId);
            st.registerOutParameter(2, OracleTypes.CURSOR);
            JdbcUtil.executeCallableStatement(st, LOGGER);
            try (ResultSet rs = st.getObject(2, ResultSet.class))
            {
                while (rs.next()) {
                    orderMovement = new OrderMovement();
                    orderMovement.setOrderId(rs.getLong(2));
                    orderMovement.setCreatedDate(rs.getTimestamp(3).toLocalDateTime());
                    if (rs.getTimestamp(4)!= null) {
                        orderMovement.setStartDate(rs.getTimestamp(4).toLocalDateTime());
                    }
                    if (rs.getTimestamp(5)!= null){
                        orderMovement.setEndDate(rs.getTimestamp(5).toLocalDateTime());
                    }
                    if (rs.getString(6)!=null) {
                        orderMovement.setStepName(rs.getString(6));
                    }
                    orderMovement.setPiid(rs.getString(8));
                    if (rs.getString(9)!=null) {
                        orderMovement.setTaskid(rs.getString(9));
                    }
                    orderMovement.setCorrelationId(rs.getString(10));
                    orderMovements.add(orderMovement);
                }
                if (orderMovements.isEmpty())
                    throw new EntityNotFoundException("OrderMovement not found for ID: "+orderId.toString());
            }
        } catch (SQLException e) {
            String s = String.format("SQLException in getByOrderId. %s", e.toString());
            LOGGER.error(s, e);
            throw e;
        }
        return orderMovements;
    }
}
