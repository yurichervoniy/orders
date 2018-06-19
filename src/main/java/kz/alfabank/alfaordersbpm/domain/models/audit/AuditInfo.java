package kz.alfabank.alfaordersbpm.domain.models.audit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@ApiModel(description = "Аудит для записей")
@MappedSuperclass
public abstract class AuditInfo {

    @ApiModelProperty(notes="Дата создания записи")
    @NotNull(message = "Дата создания не может быть пустой")
    @PastOrPresent(message = "Дата создания не может быть в будущем")
    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @ApiModelProperty(notes="Кем создана запись")
    @NotBlank(message = "Создатель не может быть пустым")
    @Size(min = 1, max = 200, message = "Длина createdBy не соотвествует установленным ограничениям")
    @Column(name = "created_by", length = 200, nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @ApiModelProperty(notes="Дата обновления записи")
    @Column(name = "modified_date")
    @PastOrPresent(message = "updated не может быть в будущем")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @ApiModelProperty(notes="Кем обновлена запись")
    @Size(min = 1, max = 200, message = "Длина modifiedBy не соотвествует установленным ограничениям")
    @Column(name = "modified_by",length = 200)
    @LastModifiedBy
    private String modifiedBy;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
