package kz.alfabank.alfaordersbpm.domain.service.order;

import java.sql.SQLException;
import java.util.Map;

public interface RetailOrderProperties {

    Map<String, String> getOrderProperties(long orderId) throws SQLException;

    Map<String, String> getOrderServiceInfo(Long orderId) throws SQLException;

}
