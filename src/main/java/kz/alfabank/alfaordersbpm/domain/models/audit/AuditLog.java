package kz.alfabank.alfaordersbpm.domain.models.audit;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_log_gen")
    @SequenceGenerator(name = "audit_log_gen", sequenceName = "audit_log_seq", allocationSize = 1)
    private Long id;

    @Column(name = "stamp", nullable = false, updatable = false)
    private LocalDateTime stamp = LocalDateTime.now();

    @Column(name = "log_date", nullable = false, updatable = false)
    private LocalDate logDate = stamp.toLocalDate();

    @Column(name = "log_time", nullable = false, updatable = false)
    private LocalTime logTime = stamp.toLocalTime();

    @Column(name = "user_name", nullable = false, updatable = false, length = 1000)
    private String username;

    @Column(name = "event_name", nullable = false, updatable = false, length = 1000)
    private String eventName;

    @Column(name = "is_success", nullable = false, updatable = false)
    private Boolean isSuccess;

    @Column(name = "ip_address", nullable = false, updatable = false)
    private String ipAddress;

    @Column(name = "machine_name", nullable = false, updatable = false, length = 1000)
    private String machineName;

    @Column(name = "method_name", nullable = false, updatable = false, length = 1000)
    private String methodName;

    @Lob
    @Basic(fetch=LAZY)
    @Column(name = "method_args", updatable = false)
    private String methodArgs;

    @Lob
    @Basic(fetch=LAZY)
    @Column(name = "method_result", updatable = false)
    private String result;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStamp() {
        return stamp;
    }

    public void setStamp(LocalDateTime stamp) {
        this.stamp = stamp;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public LocalTime getLogTime() {
        return logTime;
    }

    public void setLogTime(LocalTime logTime) {
        this.logTime = logTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodArgs() {
        return methodArgs;
    }

    public void setMethodArgs(String methodArgs) {
        this.methodArgs = methodArgs;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "id=" + id +
                ", stamp=" + stamp +
                ", logDate=" + logDate +
                ", logTime=" + logTime +
                ", username='" + username + '\'' +
                ", eventName='" + eventName + '\'' +
                ", isSuccess=" + isSuccess +
                ", ipAddress='" + ipAddress + '\'' +
                ", machineName='" + machineName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", methodArgs='" + methodArgs + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
