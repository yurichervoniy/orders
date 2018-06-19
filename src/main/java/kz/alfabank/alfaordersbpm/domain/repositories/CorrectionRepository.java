package kz.alfabank.alfaordersbpm.domain.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.alfabank.alfaordersbpm.domain.models.approval.ApprovalModel;
import kz.alfabank.alfaordersbpm.domain.models.approval.FieldsModel;
import kz.alfabank.alfaordersbpm.domain.models.approval.ServicesModel;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;

@Repository
public class CorrectionRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ApprovalRepository.class);
    private DataSource dataSource;

    @Autowired
    public CorrectionRepository(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private enum DBCmd {
        GET_CORRECTION_DATA("BEGIN pkg_approval_api.get_correction_data(:p_order_id, :p_service, :cursor); END;", 20);

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

    private FieldsModel getFieldsJson(Clob clob) throws SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(clob.getSubString(1, (int) clob.length()), FieldsModel.class);
        } catch (IOException e) {
            LOG.error("getFieldsJson", e);
            throw new IllegalArgumentException("getFieldsJson:  "+e);
        } catch (SQLException e) {
            LOG.error("getFieldsJson", e);
            throw e;
        }
    }

    public ApprovalModel getCorrectionData(Long orderId, String service, Boolean needContent) throws SQLException {
        DBCmd cmd = DBCmd.GET_CORRECTION_DATA;
        try(Connection con = dataSource.getConnection();
            CallableStatement st = con.prepareCall(cmd.getCmd())
        ) {
            st.setEscapeProcessing(false);
            st.setQueryTimeout(cmd.getTimeOut());

            st.setLong(1, orderId);
            st.setString(2, service);
            st.registerOutParameter(3, OracleTypes.CURSOR);
            executeCallableStatement(st);

            try (ResultSet rset = st.getObject(3, ResultSet.class)) {
                if (!rset.isBeforeFirst())
                    throw new EntityNotFoundException("Correction data not found for ID: "+orderId.toString());

                ApprovalModel approvalModel = new ApprovalModel();
                while (rset.next()) {
                    approvalModel.setOrderId(rset.getLong(1));
                    approvalModel.setCreatedDate(rset.getString(2));
                    approvalModel.setService(rset.getString(3));
                    approvalModel.setComment(rset.getString(4));
                    approvalModel.setGeneralCommentForManager(rset.getString(5));
                    approvalModel.setRecommendedAmount(rset.getBigDecimal(6));
                    approvalModel.setCanChangeAmount(1 == rset.getInt(7));
                    approvalModel.setOrderTime(rset.getString(8));
                    approvalModel.setCountOfRejectDays(rset.getBigDecimal(9));

                    if (rset.getString(10) != null) {
                        approvalModel.setDecision(new ServicesModel(
                                rset.getString(10),
                                rset.getString(11),
                                rset.getString(12)));
                    }

                    if (rset.getString(13) != null) {
                        approvalModel.setAdditionalInfo(new ServicesModel(
                                rset.getString(13),
                                rset.getString(14),
                                rset.getString(15)));
                    }

                    if (rset.getString(16) != null) {
                        approvalModel.setNegativeInfo(new ServicesModel(
                                rset.getString(16),
                                rset.getString(17),
                                rset.getString(18)));
                    }

                    if (rset.getString(19) != null) {
                        approvalModel.setIdCardCheck(new ServicesModel(
                                rset.getString(19),
                                rset.getString(20),
                                rset.getString(21)));
                    }

                    if (rset.getString(22) != null) {
                        approvalModel.setJudgeDBCheck(new ServicesModel(
                                rset.getString(22),
                                rset.getString(23),
                                rset.getString(24)));
                    }

                    if (rset.getString(25) != null) {
                        approvalModel.setBirthAndResidencePlace(new ServicesModel(
                                rset.getString(25),
                                rset.getString(26),
                                rset.getString(27)));
                    }

                    approvalModel.setIsParsed(rset.getLong(28));
                    approvalModel.setStatus(rset.getLong(29));

                    if (needContent) {
                        Clob clob = rset.getClob(30);
                        if (clob != null) {
                            approvalModel.setContent(getFieldsJson(clob));
                            clob.free();
                        } else {
                            approvalModel.setContent(null);
                        }
                    }
                }

                return approvalModel;
            }
        } catch (SQLException e) {
            LOG.error("getHistory", e);
            throw e;
        }
    }
}
