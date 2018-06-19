package kz.alfabank.alfaordersbpm.security;

import kz.alfabank.alfaordersbpm.domain.models.OrderBinding;
import kz.alfabank.alfaordersbpm.domain.models.exception.PermissionEvaluatorException;
import kz.alfabank.alfaordersbpm.util.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Persistable;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.stream.Collectors;

@Component
public class DefaultPermissionEvaluator implements PermissionEvaluator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPermissionEvaluator.class);
    private static final String CMD = "BEGIN :result := permission_resolver.haspermission(:p_username, :p_authorities, :p_targettype, :p_targetid, :p_permission); END;";
    private static final int TIME_OUT = 15;

    private final DataSource dataSource;

    public DefaultPermissionEvaluator(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        LOGGER.debug("hasPermission on targetDomainObject {}", targetDomainObject);
        if (targetDomainObject instanceof Persistable){
            Persistable persistable = (Persistable) targetDomainObject;
            return hasPermission(authentication, persistable.getId().toString(), targetDomainObject.getClass().getSimpleName().toUpperCase(), permission);
        } else if (targetDomainObject instanceof OrderBinding){
            OrderBinding orderBinding = (OrderBinding) targetDomainObject;
            return hasPermission(authentication, orderBinding.getOrderId().toString(), targetDomainObject.getClass().getSimpleName().toUpperCase(), permission);
        }

        LOGGER.error("PermissionEvaluator hasPermission targetDomainObject is not instanceof Persistable");
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        LOGGER.debug("hasPermission userName={} targetType={} targetId={} permission={}", authentication.getName(), targetType, targetId, permission);
        if (targetType == null || !(permission instanceof String)) {
            LOGGER.warn("PermissionEvaluator hasPermission Params not valid");
        }

        return hasPrivilege(authentication
                , targetType == null ? null : targetType.toUpperCase()
                , targetId == null ? null : targetId.toString(),
                  permission == null ? null : permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(Authentication auth, String targetType, String targetId, String permission) {
        String authoritiesCommaSeparated = auth.getAuthorities().stream()
                                            .map(GrantedAuthority::getAuthority)
                                            .collect(Collectors.joining(","));
        LOGGER.debug("hasPrivilege username={} authorities={} ", auth.getName(), authoritiesCommaSeparated);
        return resolvePermissionInDB(auth.getName(), authoritiesCommaSeparated, targetType, targetId, permission);
    }

    private boolean resolvePermissionInDB(String userName, String authorities, String targetType, String targetId, String permission) {
        LOGGER.debug("resolvePermissionInDB CMD FOR EXECUTE {}", CMD);
        try (Connection con = dataSource.getConnection();
             CallableStatement st = con.prepareCall(CMD)) {
            st.setEscapeProcessing(false);
            st.setQueryTimeout(TIME_OUT);
            st.registerOutParameter(1, Types.INTEGER);
            st.setString(2, userName);
            st.setString(3, authorities);
            st.setString(4, targetType);
            st.setString(5, targetId);
            st.setString(6, permission);
            JdbcUtil.executeCallableStatement(st, LOGGER);
            boolean hasAccess = JdbcUtil.intToBoolean(st.getInt(1));
            LOGGER.debug("resolvedPermissionInDB for user {} hasAccess={}", userName, hasAccess);
            return hasAccess;
        } catch (SQLException e) {
            LOGGER.error("SQLException in resolvePermissionInDB {}", e);
            throw new PermissionEvaluatorException("Exception in resolvePermissionInDB " + e.getMessage(), e);
        }
    }


}
