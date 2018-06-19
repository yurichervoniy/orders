package kz.alfabank.alfaordersbpm.util;

import org.slf4j.Logger;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Arrays;


public final class JdbcUtil {
    private JdbcUtil(){}

    public static void executeCallableStatement(CallableStatement stm, Logger logger) throws SQLException {
        try {
            stm.execute();
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            if ("72000".equals(e.getSQLState()) && (errorCode == 4068 || errorCode == 4061 || errorCode == 4065 || errorCode == 4067)) {
                logger.warn(String.format("executeRetry=>%s", Arrays.toString(Thread.currentThread().getStackTrace())), e);
                stm.execute(); //Retry Execution on Recoverable Exceptions
            } else
                throw e;
        }
    }

    public static int booleanToInt(boolean b){
        return b ? 1 : 0;
    }

    public static boolean intToBoolean(int i){
        return i != 0;
    }

}
