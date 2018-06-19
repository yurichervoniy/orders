package kz.alfabank.alfaordersbpm.domain.models.approval;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.attachment.Attachment;
import kz.alfabank.alfaordersbpm.domain.models.audit.AuditInfo;
import org.hibernate.annotations.RowId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_files_correction")
@RowId("ROWID")
@EntityListeners(AuditingEntityListener.class)
public class FileCorrection extends AuditInfo {
    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_correct_gen")
    @SequenceGenerator(name = "file_correct_gen", sequenceName = "s_order_files_correction", allocationSize = 1)
    private Long id;

    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @NotNull(message = "Для коррекции файла обязательно ID заявки")
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="attach_id")
    private Attachment attachId;

    @ApiModelProperty(notes="Дата корректировки файла")
    @Column(name = "last_modified")
    @PastOrPresent(message = "modified_date не может быть в будущем")
    private LocalDateTime lastModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Attachment getAttachId() {
        return attachId;
    }

    public void setAttachId(Attachment attachId) {
        this.attachId = attachId;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "FileCorrection{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", attachId=" + attachId +
                ", lastModified=" + lastModified +
                '}';
    }
}
