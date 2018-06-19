package kz.alfabank.alfaordersbpm.domain.service.bpmocrm.bpmadapter;

import kz.alfabank.alfaordersbpm.components.MessageParser;
import kz.alfabank.alfaordersbpm.config.BpmCrmProperties;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.*;
import kz.alfabank.alfaordersbpm.domain.models.exception.BpmAdapterException;
import kz.alfabank.alfaordersbpm.domain.models.exception.InternalServerException;
import kz.alfabank.alfaordersbpm.util.JdbcUtil;
import kz.alfabank.alfaordersbpm.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import javax.sql.DataSource;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class BpmAdapterProxyImpl implements BpmAdapterProxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(BpmAdapterProxyImpl.class);
    private static final String CMD = "BEGIN retail_orders.getbpmstartprocessparams(:bpdid, :branchid, :processAppID); END;";
    private static final int TIME_OUT = 10;
    private static final Long SUCCESS_RETCODE = 0l;
    private static final Long FORBIDDEN_RETCODE = -401l;
    private static final int CONNECT_TIMEOUT = 4000;
    private static final int READ_TIMEOUT = 15000;
    private static final String PROCESS_PATH_SEGMENT = "{processId}";
    private static final String PROCESS_ID_SEGMENT = "processId";
    private static final String ACTION_QUERY_PARAM = "action";
    private static final String TO_USER = "toUser";
    private static final String ACTION_START = "start";
    private static final String BPD_ID = "bpdId";
    private static final String BRANCH_ID = "branchId";
    private static final String APP_ID = "processAppId";
    private static final String PARAMS = "params";
    private static final String PARTS = "parts";
    private static final String HEADER = "header";
    private static final String DATA = "data";
    private static final List<String> HEADER_DATA = Arrays.asList(HEADER, DATA);
    private static final String TASK_PATH_SEGMENT = "{taskId}";
    private static final String TASK_ID_SEGMENT = "taskId";
    //Create Request
    private static final String INSTANCE_ID = "instanceId";
    private static final String LOGIN = "login";
    private static final String ENTITY_NAME = "entityName";

    private final String processPath;
    private final String setTaskParamsPath;
    private final String taskPath;
    private final String exposedProcess;
    private final String sendMessage;

    @NotBlank
    private final String url;

    @NotBlank
    private final String createRequestUrl;

    private final RestTemplate restTemplate;
    @Autowired
    private MessageParser messageParser;

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @NotBlank
    private final String authHeaderValue;

    @Autowired
    public BpmAdapterProxyImpl(@Value("${bpmAdapter.processpath}") final String processPath,
                               @Value("${bpmAdapter.taskparams}") final String setTaskParamsPath,
                               @Value("${bpmAdapter.taskpath}") final String taskPath,
                               @Value("${bpmAdapter.exposedprocess}") final String exposedProcess,
                               @Value("${bpmAdapter.sendmessage}") final String sendMessage,
                               RestTemplateBuilder restTemplateBuilder,
                               BpmCrmProperties bpmCrmProperties) {
        this.processPath = processPath;
        this.setTaskParamsPath = setTaskParamsPath;
        this.taskPath = taskPath;
        this.exposedProcess = exposedProcess;
        this.sendMessage = sendMessage;
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setReadTimeout(READ_TIMEOUT)
                .build();
        url = bpmCrmProperties.getBpmAdapterUrl().toString();
        createRequestUrl = bpmCrmProperties.getBpmCreateRequestUrl().toString();
        String auth = bpmCrmProperties.getBpm().getUser() + ":" + bpmCrmProperties.getBpm().getPwd();
        authHeaderValue = Base64Utils.encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }

    private StartProcessRequest getStartProcessRequest(BpmInputRequest bpmInputRequest) {
        LOGGER.debug("getStartProcessParams CMD FOR EXECUTE {}", CMD);
        try (Connection con = dataSource.getConnection();
             CallableStatement st = con.prepareCall(CMD)) {
            st.setEscapeProcessing(false);
            st.setQueryTimeout(TIME_OUT);
            st.registerOutParameter(1, Types.VARCHAR);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            JdbcUtil.executeCallableStatement(st, LOGGER);
            return new StartProcessRequest(st.getString(1), st.getString(2), st.getString(3), bpmInputRequest);
        } catch (SQLException e) {
            LOGGER.error("SQLException in getStartProcessRequestParams {}", e);
            throw new BpmAdapterException("Ошибка получения параметров для запуска процесса " + e.getMessage(), e);
        }
    }

    private UriComponentsBuilder getProcessUriComponentsBuilder(){
        return UriComponentsBuilder.fromHttpUrl(url)
                .path(processPath)
                ;
    }

    private HttpHeaders createBasicAuthHeader(boolean isSystemUser){
        HttpHeaders headers = new HttpHeaders();
        if (isSystemUser){
            headers.add(RequestUtil.BASIC_AUTH_HEADER, authHeaderValue);
        } else {
            headers.add(RequestUtil.BASIC_AUTH_HEADER, RequestUtil.getBasicAuthHeader());
        }
        return headers;
    }

    private <T extends DefaultBpmAdapterResponse> T parseBpmAdapterResponse(String body, Class<T> valueType){
        try {
            T bpmAdapterResponse = messageParser.tryParseMessage(body, valueType);
            Long retCode = bpmAdapterResponse.getRetCode();
            if (FORBIDDEN_RETCODE.equals(retCode))
                throw new AccessDeniedException(String.format("BpmAdapterException Доступ запрещен RETTEXT=%s BODY=%s", bpmAdapterResponse.getRetText(), body));
            if (retCode == null || SUCCESS_RETCODE.compareTo(retCode) >= 0)
                throw new BpmAdapterException(String.format("Ошибка BpmAdapter RETCODE %s, RETTEXT=%s BODY=%s", bpmAdapterResponse.getRetCode(), bpmAdapterResponse.getRetText(), body));
            return bpmAdapterResponse;
        }
        catch (BpmAdapterException|AccessDeniedException be){
            throw be;
        }
        catch (Exception ex){
            LOGGER.error("Error in parseBpmAdapterResponse", ex);
            throw new BpmAdapterException("Ошибка при разборе ответа BpmAdapter parseBpmAdapterResponse " + ex.getMessage(), ex);
        }
    }

    private <T extends DefaultBpmAdapterResponse> T invokeBpmAdapter(UriComponentsBuilder builder, Map<String, String> uriParams, Object bodyParams, HttpMethod httpMethod, Class<T> valueType, boolean isSystemUser){
        String toUriString = builder.toUriString();
        LOGGER.debug("invokeBpmAdapter httpMethod={} uriParams={} bodyParams={} builder={}", httpMethod, uriParams, bodyParams, toUriString);
        URI uri = null;
        HttpEntity<?> request = null;
        try {
            HttpHeaders headers = createBasicAuthHeader(isSystemUser);
            request = new HttpEntity<>(bodyParams, headers);
            uri = uriParams == null ? builder.build().toUri() : builder.buildAndExpand(uriParams).toUri();
            LOGGER.trace("invokeBpmAdapter uri {} request {}", uri, request);
            ResponseEntity<String> response = restTemplate.exchange(uri, httpMethod, request, String.class);
            String body = response.getBody();
            if (StringUtils.isEmpty(body))
                throw new BpmAdapterException("Ошибка вызова BpmAdapter RESPONSE BODY is NULL or empty");
            return parseBpmAdapterResponse(body, valueType);
        }
        catch (BpmAdapterException|AccessDeniedException be){
            LOGGER.error("Exception in invokeBpmAdapter uri {} httpMethod={} request {}", uri, httpMethod, request);
            throw be;
        }
        catch (HttpStatusCodeException se){
            LOGGER.error("invokeBpmAdapter -> HttpStatusCodeException", se);
            LOGGER.error("Exception in invokeBpmAdapter HttpStatusCodeException uri {} httpMethod={} request {}", uri, httpMethod, request);
            throw new BpmAdapterException("Ошибка вызова BpmAdapter " + se.getMessage() + se.getResponseBodyAsString(), se);
        }
        catch (Exception e){
            LOGGER.error("Error in invokeBpmAdapter", e);
            LOGGER.error("Exception in invokeBpmAdapter HttpStatusCodeException uri {} httpMethod={} request {}", uri, httpMethod, request);
            throw new BpmAdapterException("Ошибка вызова BpmAdapter " + e.getMessage(), e);
        }
    }

    private <T extends DefaultBpmAdapterResponse> T invokeBpmAdapter(UriComponentsBuilder builder, Map<String, String> uriParams, Object bodyParams, HttpMethod httpMethod, Class<T> valueType){
        return invokeBpmAdapter(builder, uriParams, bodyParams, httpMethod, valueType, false);
    }


    @Override
    public ProcessInfoResponse startProcess(BpmInputRequest bpmInputRequest) {
        LOGGER.debug("trying to startProcess with request {}", bpmInputRequest);
        StartProcessRequest startProcessRequest = getStartProcessRequest(bpmInputRequest);
        try {
            String params = messageParser.writeValueAsString(startProcessRequest);
            LOGGER.debug("params before urlencode {}", params);
            UriComponentsBuilder builder = getProcessUriComponentsBuilder()
                    .queryParam(ACTION_QUERY_PARAM, ACTION_START)
                    .queryParam(BPD_ID, startProcessRequest.getBpdId())
                    .queryParam(BRANCH_ID, startProcessRequest.getBranchId())
                    .queryParam(APP_ID, startProcessRequest.getProcessAppID())
                    .queryParam(PARTS, String.join(",", HEADER_DATA))
                    ;
            MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
            bodyParams.add(PARAMS, UriUtils.encode(params, StandardCharsets.UTF_8));
            ProcessInfoResponse processInfoResponse = invokeBpmAdapter(builder, null, bodyParams, HttpMethod.POST, ProcessInfoResponse.class);
            if (processInfoResponse.getProcessData().getPiid() == null)
                throw new BpmAdapterException(String.format("Идентификатор процесса равен NULL, RESPONSE BODY=%s", processInfoResponse));
            return processInfoResponse;
        }
        catch (BpmAdapterException|AccessDeniedException be){
            throw be;
        }
        catch (Exception e){
            LOGGER.error("Error in bpm startProcess", e);
            throw new BpmAdapterException("Ошибка в запуске прцоесса BpmAdapter(startProcess) " + e.getMessage(), e);
        }
    }

    @Override
    public ProcessInfoResponse getProcessData(String processId) {
        LOGGER.debug("trying to getProcessData for processId {}", processId);
        if (processId == null)
            throw new IllegalArgumentException("Для получения данных по процессу, идентификатор processId не может быть null");
        try {
            UriComponentsBuilder build = getProcessUriComponentsBuilder()
                    .pathSegment(PROCESS_PATH_SEGMENT)
                    .queryParam(PARTS, HEADER_DATA)
                    ;
            Map<String, String> uriParams = new HashMap<>();
            uriParams.put(PROCESS_ID_SEGMENT, processId);
            return invokeBpmAdapter(build, uriParams, null, HttpMethod.GET, ProcessInfoResponse.class);
        }
        catch (BpmAdapterException|AccessDeniedException be){
            throw be;
        }
        catch (Exception e){
            LOGGER.error("Error in bpm getProcessData", e);
            throw new BpmAdapterException("Ошибка BpmAdapter при получении данных прцоесса " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getProcessVariables(String processId) {
        LOGGER.debug("trying to getProcessVariables for processId {}", processId);
        if (processId == null)
            throw new IllegalArgumentException("Для получения переменных по процессу, идентификатор processId не может быть null");
        try {
            UriComponentsBuilder build = getProcessUriComponentsBuilder()
                    .pathSegment(PROCESS_PATH_SEGMENT)
                    .queryParam(PARTS, DATA)
                    ;
            Map<String, String> uriParams = new HashMap<>();
            uriParams.put(PROCESS_ID_SEGMENT, processId);
            ProcessInfoResponse processInfoResponse = invokeBpmAdapter(build, uriParams, null, HttpMethod.GET, ProcessInfoResponse.class);
            Map<String, Object> variables = processInfoResponse.getProcessData().getVariables();
            if (variables == null)
                throw new BpmAdapterException("Значение variables в getProcessVariables равно null");
            return variables;
        }
        catch (BpmAdapterException|AccessDeniedException be){
            throw be;
        }
        catch (Exception e){
            LOGGER.error("Error in bpm getProcessVariables", e);
            throw new BpmAdapterException("Ошибка BpmAdapter при получении переменных прцоесса " + e.getMessage(), e);
        }
    }

    @Override
    public String getProcessStepUI(String processId) {
        Map<String, Object> prcessVariables = getProcessVariables(processId);
        if(prcessVariables == null || prcessVariables.size() == 0){
            throw new BpmAdapterException("Переменные процесса is null or empty in getProcessStepUI");
        }
        return Optional.ofNullable(prcessVariables.get(ProcessData.STEP_UI))
                        .orElseThrow(()->new BpmAdapterException(String.format("Переменная с кодом %s не найдена", ProcessData.STEP_UI)))
                .toString();
    }

    @Override
    public List<Task> getProcessTasks(String processId){
        LOGGER.debug("getProcessTasks for processId {}", processId);
        if (processId == null)
            throw new IllegalArgumentException("Для получения задач по процессу, идентификатор processId не может быть null");
        try {
            UriComponentsBuilder build = getProcessUriComponentsBuilder()
                    .pathSegment(PROCESS_PATH_SEGMENT)
                    .queryParam(PARTS, HEADER)
                    ;
            Map<String, String> uriParams = new HashMap<>();
            uriParams.put(PROCESS_ID_SEGMENT, processId);
            ProcessInfoResponse processInfoResponse = invokeBpmAdapter(build, uriParams, null, HttpMethod.GET, ProcessInfoResponse.class);
            List<Task> tasks = processInfoResponse.getProcessData().getTasks();
            if (tasks == null || tasks.isEmpty()){
                LOGGER.warn("No tasks in getProcessTasks for processId {}", processId);
                return Collections.emptyList();
            }
            return tasks;
        }
        catch (BpmAdapterException|AccessDeniedException be){
            throw be;
        }
        catch (Exception e){
            LOGGER.error("Error in bpm getProcessTasks", e);
            throw new BpmAdapterException("Ошибка при получении задач по процессу " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Task> getReceivedTaskByName(String processId, String taskName) {
        LOGGER.debug("getRecivedTaskByName for processId {} taskName {}", processId, taskName);
        if (processId == null)
            throw new IllegalArgumentException("Для получения getReceivedTaskByName по процессу, идентификатор processId не может быть null");
        if (taskName == null)
            throw new IllegalArgumentException("Для получения getReceivedTaskByName по процессу, идентификатор taskName не может быть null");
        List<Task> tasks = getProcessTasks(processId);
        if (tasks.isEmpty())
            return Optional.empty();
        return tasks.stream()
                .filter(t-> taskName.equals(t.getName()) && Task.RECEIVED_STATUS.equalsIgnoreCase(t.getStatus()))
                .findFirst();
    }

    @Override
    public List<Task> getReceivedTasks(String processId) {
        LOGGER.debug("getReceivedTasks for processId {}", processId);
        if (processId == null)
            throw new IllegalArgumentException("Для получения getReceivedTaskByName по процессу, идентификатор processId не может быть null");
        List<Task> tasks = getProcessTasks(processId);
        if (tasks.isEmpty())
            return tasks;
        return tasks.stream()
                .filter(t-> Task.RECEIVED_STATUS.equalsIgnoreCase(t.getStatus()))
                .collect(Collectors.toList())
                ;
    }

    @Override
    public Task getTaskOrElseThrow(String processId, String taskName) {
        return getReceivedTaskByName(processId, taskName)
                .orElseThrow(()-> new InternalServerException(String.format("Задача в статусе %s для processId=%s с наименованием=%s не найдена, проверьте процесс BPM", Task.RECEIVED_STATUS, processId, taskName)));
    }

    @Override
    public Task getTaskById(String taskId) {
        LOGGER.debug("trying to getTaskById {}", taskId);
        if (taskId == null)
            throw new IllegalArgumentException("Для получения задачи по ИД идентификатор taskId не может быть null");
        try {
            UriComponentsBuilder build = UriComponentsBuilder.fromHttpUrl(url)
                    .path(taskPath)
                    .pathSegment(TASK_PATH_SEGMENT)
                    .queryParam(PARTS, HEADER)
                    ;
            Map<String, String> uriParams = new HashMap<>();
            uriParams.put(TASK_ID_SEGMENT, taskId);

            TaskInfoResponse taskInfoResponse = invokeBpmAdapter(build, uriParams, null, HttpMethod.GET, TaskInfoResponse.class);
            LOGGER.debug("taskInfoResponse {}", taskInfoResponse);
            Task task = taskInfoResponse.getTask();
            if (task == null)
                throw new BpmAdapterException(String.format("Задача с id=%s не найдена в BPM", taskId));
            return task;
        }
        catch (BpmAdapterException|AccessDeniedException be){
            throw be;
        }
        catch (Exception e){
            LOGGER.error("Error in bpm getTaskById", e);
            throw new BpmAdapterException("Ошибка BpmAdapter getTaskById " + e.getMessage(), e);
        }
    }

    @Override
    public void setTaskParams(Task task, TaskResult taskResult){
        LOGGER.debug("trying to setTaskParams {} {}", task, taskResult);
        if (task == null)
            throw new IllegalArgumentException("Для setTaskParams task не может быть null");
        if (task.getTkiid() == null)
            throw new IllegalArgumentException("Для setTaskParams task Tkiid не может быть null");
        if (taskResult == null)
            throw new IllegalArgumentException("Для setTaskParams taskResult не может быть null");
        try {
            UriComponentsBuilder build = UriComponentsBuilder.fromHttpUrl(url)
                    .path(setTaskParamsPath)
                    .pathSegment(TASK_PATH_SEGMENT)
                    ;
            Map<String, String> uriParams = new HashMap<>();
            uriParams.put(TASK_ID_SEGMENT, task.getTkiid());

            String body = messageParser.writeValueAsString(new SetTaskParamsRequest(taskResult));
            DefaultBpmAdapterResponse bpmAdapterResponse = invokeBpmAdapter(build, uriParams, body, HttpMethod.PUT, DefaultBpmAdapterResponse.class);
            LOGGER.debug("setTaskParams ret code {}", bpmAdapterResponse.getRetCode());
        }
        catch (BpmAdapterException|AccessDeniedException be){
            throw be;
        }
        catch (Exception e){
            LOGGER.error("Error in bpm setTaskParams", e);
            throw new BpmAdapterException("Ошибка BpmAdapter in setTaskParams " + e.getMessage(), e);
        }
    }

    @Override
    public void setTaskStatus(Task task, TaskAction taskAction) {
        setTaskStatus(task, taskAction, null, false);
    }

    @Override
    public void setTaskStatus(Task task, TaskAction taskAction, String assignTo, boolean isSystemUser) {
        LOGGER.debug("trying to setTaskStatus {} {} {} {}", task, taskAction, assignTo, isSystemUser);
        if (task == null)
            throw new IllegalArgumentException("Для setTaskStatus, task не может быть null");
        if (task.getTkiid() == null)
            throw new IllegalArgumentException("Для setTaskStatus, Tkiid не может быть null");
        if (taskAction == null)
            throw new IllegalArgumentException("Для setTaskStatus, taskAction не может быть null");
        try {
            UriComponentsBuilder build = UriComponentsBuilder.fromHttpUrl(url)
                    .path(taskPath)
                    .pathSegment(TASK_PATH_SEGMENT)
                    .queryParam(PARTS, HEADER)
                    ;
            Map<String, String> uriParams = new HashMap<>();
            uriParams.put(TASK_ID_SEGMENT, task.getTkiid());

            MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
            bodyParams.add(ACTION_QUERY_PARAM, taskAction.name().toLowerCase());
            if (!StringUtils.isEmpty(assignTo)) {
                bodyParams.add(TO_USER, UriUtils.encode(assignTo, StandardCharsets.UTF_8));
            }

            DefaultBpmAdapterResponse bpmAdapterResponse = invokeBpmAdapter(build, uriParams, bodyParams, HttpMethod.PUT, DefaultBpmAdapterResponse.class, isSystemUser);
            LOGGER.debug("setTaskStatus ret code {}", bpmAdapterResponse.getRetCode());
        }
        catch (BpmAdapterException|AccessDeniedException be){
            throw be;
        }
        catch (Exception e){
            LOGGER.error("Error in bpm setTaskStatus", e);
            throw new BpmAdapterException("Ошибка BpmAdapter setTaskStatus " + e.getMessage(), e);
        }
    }

    @Override
    public void completeTask(Task task, TaskResult taskResult) {
        LOGGER.debug("trying to completeTask {} {}", task, taskResult);
        if (task == null)
            throw new IllegalArgumentException("Для completeTask task не может быть null");
        if (task.getTkiid() == null)
            throw new IllegalArgumentException("Для completeTask Tkiid не может быть null");
        if (taskResult == null)
            throw new IllegalArgumentException("Для completeTask taskResult не может быть null");
        try {
            setTaskParams(task, taskResult);
            setTaskStatus(task, TaskAction.FINISH);
        }
        catch (BpmAdapterException be){
            throw be;
        }
        catch (Exception e){
            LOGGER.error("Error in bpm completeTask", e);
            throw new BpmAdapterException("Ошибка BpmAdapter completeTask " + e.getMessage(), e);
        }
    }

    @Override
    public boolean exposeToStartByAcronym(String acronym) {
        LOGGER.debug("trying to exposeToStartByAcronym {}", acronym);
        if (acronym == null)
            throw new IllegalArgumentException("Для exposeToStartByAcronym acronym не может быть null");
        try {
            UriComponentsBuilder build = UriComponentsBuilder.fromHttpUrl(url)
                    .path(exposedProcess)
                    ;

            ExposedProcessRespponse bpmAdapterResponse = invokeBpmAdapter(build, null, null, HttpMethod.GET, ExposedProcessRespponse.class);
            LOGGER.debug("exposeToStartByAcronym ret code {}", bpmAdapterResponse.getRetCode());
            ExposedData exposedData = bpmAdapterResponse.getExposedData();
            if (exposedData == null) {
                throw new BpmAdapterException("Ошибка BpmAdapter in exposeToStartByAcronym exposedData is null");
            }

            List<ExposedItem> exposedItems = exposedData.getExposedItemsList();
            if (exposedItems == null || exposedItems.isEmpty()){
                return false;
            }

            long count =exposedItems.stream()
                    .filter(e-> Objects.equals(acronym, e.getProcessAppAcronym()))
                    .count();

            return count > 0;
        }
        catch (BpmAdapterException|AccessDeniedException be){
            throw be;
        }
        catch (Exception e){
            LOGGER.error("Error in bpm exposeToStartByAcronym", e);
            throw new BpmAdapterException("Ошибка BpmAdapter in exposeToStartByAcronym " + e.getMessage(), e);
        }
    }

    @Override
    public Long createRequest(String instanceId, String login, String entityName) {
        LOGGER.debug("trying to createRequest ProcService instanceId={} login={} entityName={}", instanceId, login, entityName);
        if (instanceId == null)
            throw new IllegalArgumentException("Для createRequest, instanceId не может быть null");
        if (login == null)
            throw new IllegalArgumentException("Для createRequest, login не может быть null");
        if (entityName == null)
            throw new IllegalArgumentException("Для createRequest, entityName не может быть null");
        try {
            UriComponentsBuilder build = UriComponentsBuilder.fromHttpUrl(createRequestUrl);

            Map<String, String> uriParams = new HashMap<>();
            uriParams.put(INSTANCE_ID, UriUtils.encode(instanceId, StandardCharsets.UTF_8));
            uriParams.put(LOGIN, UriUtils.encode(login, StandardCharsets.UTF_8));
            uriParams.put(ENTITY_NAME, UriUtils.encode(entityName, StandardCharsets.UTF_8));

            DefaultBpmAdapterResponse bpmAdapterResponse = invokeBpmAdapter(build, uriParams, null, HttpMethod.GET, DefaultBpmAdapterResponse.class);
            Long result = bpmAdapterResponse.getRetCode();
            LOGGER.debug("createRequest parsed requestid {}", result);
            return result;
        }
        catch (BpmAdapterException|AccessDeniedException be){
            throw be;
        }
        catch (Exception e){
            LOGGER.error("Error in bpm finishTask", e);
            throw new BpmAdapterException("Ошибка BpmAdapter finishTask " + e.getMessage(), e);
        }
    }

    @Override
    public void sendMessage(String message) {
        LOGGER.debug("trying to sendMessage {}", message);
        if (message == null)
            throw new IllegalArgumentException("To sendMessage message mus not be null");
        try {
            UriComponentsBuilder build = UriComponentsBuilder.fromHttpUrl(url)
                    .path(sendMessage);
            DefaultBpmAdapterResponse bpmAdapterResponse = invokeBpmAdapter(build, null, message, HttpMethod.POST, DefaultBpmAdapterResponse.class, true);
            LOGGER.debug("sendMessage ret code {}", bpmAdapterResponse.getRetCode());
        }
        catch (BpmAdapterException be){
            throw be;
        }
        catch (Exception e){
            LOGGER.error("Error in bpm sendMessage", e);
            throw new BpmAdapterException("Exception in sendMessage " + e.getMessage(), e);
        }
    }


}
