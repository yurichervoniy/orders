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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class ApprovalRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ApprovalRepository.class);
    private DataSource dataSource;

    @Autowired
    public ApprovalRepository(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private enum DBCmd {
        SAVE_APPROVAL_FIELDS("BEGIN pkg_approval_api.save_approval_fields(" +
                ":p_order_id, " +
                ":p_service, " +
                ":p_comments, " +
                ":p_general_comment, " +
                ":p_rec_amount, " +
                ":p_can_chg_amount, " +
                ":p_order_time, " +
                ":p_count_of_reject_days, " +
                ":p_decision_text, " +
                ":p_decision_value, " +
                ":p_decision_dict, " +
                ":p_additional_info_text, " +
                ":p_additional_info_value, " +
                ":p_additional_info_dict, " +
                ":p_negative_info_text, " +
                ":p_negative_info_value, " +
                ":p_negative_info_dict, " +
                ":p_idcard_check_text, " +
                ":p_idcard_check_value, " +
                ":p_idcard_check_dict, " +
                ":p_judge_db_check_text, " +
                ":p_judge_db_check_value, " +
                ":p_judge_db_check_dict, " +
                ":p_birth_and_res_place_text, " +
                ":p_birth_and_res_place_value, " +
                ":p_birth_and_res_place_dict, " +
                ":p_fields); END;", 20),
        GET_APPROVAL_HISTORY("BEGIN pkg_approval_api.get_approval_history(:p_order_id, :p_service, :cursor); END;", 20);

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
                LOG.warn(String.format("executeRetry=> %s", Arrays.toString(Thread.currentThread().getStackTrace())), e);
                stm.execute(); //Retry Execution on Recoverable Exceptions
            } else
                throw e;
        }
    }

    private String getFieldsClob(FieldsModel fieldsModel) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonInString = mapper.writeValueAsString(fieldsModel);
            LOG.debug(jsonInString);
            return jsonInString;
        } catch (IOException e) {
            LOG.error("Ошибка JSON", e);
            throw new IllegalArgumentException("getFieldsClob: "+e);
        }
    }

    private FieldsModel getFieldsJson(Clob clob) throws SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(clob.getSubString(1, (int) clob.length()), FieldsModel.class);
        } catch (IOException e) {
            LOG.error("getFieldsJson", e);
            throw new IllegalArgumentException("getFieldsJson:"+e);
        } catch (SQLException e) {
            LOG.error("getFieldsJson", e);
            throw e;
        }
    }

    private void setInParams(CallableStatement st, ApprovalModel approvalModel) throws SQLException {
        st.setLong(1, approvalModel.getOrderId());
        st.setString(2, approvalModel.getService());
        st.setString(3, approvalModel.getComment());
        st.setString(4, approvalModel.getGeneralCommentForManager());
        st.setBigDecimal(5, approvalModel.getRecommendedAmount());
        if (approvalModel.getCanChangeAmount() != null) {
            st.setBigDecimal(6, BigDecimal.valueOf(approvalModel.getCanChangeAmount() ? 1 : 0));
        } else {
            st.setBigDecimal(6, null);
        }
        st.setString(7, approvalModel.getOrderTime());
        st.setBigDecimal(8, approvalModel.getCountOfRejectDays());
        st.setString(9, approvalModel.getDecision() != null ? approvalModel.getDecision().getText() : null);
        st.setString(10, approvalModel.getDecision() != null ? approvalModel.getDecision().getValue() : null);
        st.setString(11, approvalModel.getDecision() != null ? approvalModel.getDecision().getDictName() : null);
        st.setString(12, approvalModel.getAdditionalInfo() != null ? approvalModel.getAdditionalInfo().getText() : null);
        st.setString(13, approvalModel.getAdditionalInfo() != null ? approvalModel.getAdditionalInfo().getValue() : null);
        st.setString(14, approvalModel.getAdditionalInfo() != null ? approvalModel.getAdditionalInfo().getDictName() : null);
        st.setString(15, approvalModel.getNegativeInfo() != null ? approvalModel.getNegativeInfo().getText() : null);
        st.setString(16, approvalModel.getNegativeInfo() != null ? approvalModel.getNegativeInfo().getValue() : null);
        st.setString(17, approvalModel.getNegativeInfo() != null ? approvalModel.getNegativeInfo().getDictName() : null);
        st.setString(18, approvalModel.getIdCardCheck() != null ? approvalModel.getIdCardCheck().getText() : null);
        st.setString(19, approvalModel.getIdCardCheck() != null ? approvalModel.getIdCardCheck().getValue() : null);
        st.setString(20, approvalModel.getIdCardCheck() != null ? approvalModel.getIdCardCheck().getDictName() : null);
        st.setString(21, approvalModel.getJudgeDBCheck() != null ? approvalModel.getJudgeDBCheck().getText() : null);
        st.setString(22, approvalModel.getJudgeDBCheck() != null ? approvalModel.getJudgeDBCheck().getValue() : null);
        st.setString(23, approvalModel.getJudgeDBCheck() != null ? approvalModel.getJudgeDBCheck().getDictName() : null);
        st.setString(24, approvalModel.getBirthAndResidencePlace() != null ? approvalModel.getBirthAndResidencePlace().getText() : null);
        st.setString(25, approvalModel.getBirthAndResidencePlace() != null ? approvalModel.getBirthAndResidencePlace().getValue() : null);
        st.setString(26, approvalModel.getBirthAndResidencePlace() != null ? approvalModel.getBirthAndResidencePlace().getDictName() : null);
    }

    @Transactional(timeout = 60, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveApprovalFields(ApprovalModel approvalModel) throws SQLException {
        DBCmd cmd = DBCmd.SAVE_APPROVAL_FIELDS;
        try(Connection con = dataSource.getConnection();
            CallableStatement st = con.prepareCall(cmd.getCmd())
        ) {
            st.setEscapeProcessing(false);
            st.setQueryTimeout(cmd.getTimeOut());

            setInParams(st, approvalModel);

            Clob clob = con.createClob();
            clob.setString(1, getFieldsClob(approvalModel.getContent()));
            st.setClob(27, clob);
            executeCallableStatement(st);
            clob.free();

        } catch (SQLException e) {
            LOG.error("Error in saveApprovalFields", e);
            throw e;
        }
    }

    public List<ApprovalModel> getHistory(Long orderId, String service, Boolean needContent) throws SQLException {
        DBCmd cmd = DBCmd.GET_APPROVAL_HISTORY;
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
                List<ApprovalModel> approvalModels = new ArrayList<>();
                ApprovalModel approvalModel;
                while (rset.next()) {
                    approvalModel = new ApprovalModel();
                    approvalModel.setOrderId(rset.getLong(1));
                    approvalModel.setCreatedDate(rset.getString(2));
                    approvalModel.setService(rset.getString(3));
                    approvalModel.setComment(rset.getString(4));
                    approvalModel.setGeneralCommentForManager(rset.getString(5));
                    approvalModel.setRecommendedAmount(rset.getBigDecimal(6));
                    if (rset.getBigDecimal(7) != null)
                        approvalModel.setCanChangeAmount(1 == rset.getInt(7));

                    approvalModel.setOrderTime(rset.getString(8));
                    approvalModel.setCountOfRejectDays(rset.getBigDecimal(9));

                    if (rset.getString(10) != null)
                        approvalModel.setDecision(new ServicesModel(
                                rset.getString(10),
                                rset.getString(11),
                                rset.getString(12)));

                    if (rset.getString(13) != null)
                        approvalModel.setAdditionalInfo(new ServicesModel(
                                rset.getString(13),
                                rset.getString(14),
                                rset.getString(15)));

                    if (rset.getString(16) != null)
                        approvalModel.setNegativeInfo(new ServicesModel(
                                rset.getString(16),
                                rset.getString(17),
                                rset.getString(18)));

                    if (rset.getString(19) != null)
                        approvalModel.setIdCardCheck(new ServicesModel(
                                rset.getString(19),
                                rset.getString(20),
                                rset.getString(21)));

                    if (rset.getString(22) != null)
                        approvalModel.setJudgeDBCheck(new ServicesModel(
                                rset.getString(22),
                                rset.getString(23),
                                rset.getString(24)));

                    if (rset.getString(25) != null)
                        approvalModel.setBirthAndResidencePlace(new ServicesModel(
                                rset.getString(25),
                                rset.getString(26),
                                rset.getString(27)));

                    approvalModel.setIsParsed(rset.getLong(28));
                    approvalModel.setStatus(rset.getLong(29));

                    if (needContent != null && needContent) {
                        Clob clob = rset.getClob(30);
                        if (clob == null) {
                            approvalModel.setContent(null);
                        } else {
                            approvalModel.setContent(getFieldsJson(clob));
                            clob.free();
                        }
                    }
                    approvalModels.add(approvalModel);
                }

                if (approvalModels.isEmpty())
                    throw new EntityNotFoundException("Approval history not found for ID: "+orderId.toString());

                return approvalModels;
            }
        } catch (SQLException e) {
            LOG.error("getHistory", e);
            throw e;
        }
    }
}
