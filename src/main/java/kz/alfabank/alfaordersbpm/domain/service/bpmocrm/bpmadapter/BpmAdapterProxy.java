package kz.alfabank.alfaordersbpm.domain.service.bpmocrm.bpmadapter;

import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BpmAdapterProxy {

    ProcessInfoResponse startProcess(BpmInputRequest bpmInputRequest);

    ProcessInfoResponse getProcessData(String processId);

    Map<String, Object> getProcessVariables(String processId);

    String getProcessStepUI(String processId);

    List<Task> getProcessTasks(String processId);

    Optional<Task> getReceivedTaskByName(String processId, String taskName);

    List<Task> getReceivedTasks(String processId);

    Task getTaskOrElseThrow(String processId, String taskName);

    Task getTaskById(String taskId);

    void setTaskParams(Task task, TaskResult taskResult);

    void setTaskStatus(Task task, TaskAction taskAction);

    void setTaskStatus(Task task, TaskAction taskAction, String assignTo, boolean isSystemUser);

    void completeTask(Task task, TaskResult taskResult);

    boolean exposeToStartByAcronym(String acronym);

    Long createRequest(String instanceId, String login, String entityName);

    void sendMessage(String message);

}
