package kz.alfabank.alfaordersbpm.domain.models.attachment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.audit.AuditInfo;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.order.AbstractOrder;
import org.hibernate.annotations.RowId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "order_attachment"
        ,uniqueConstraints={@UniqueConstraint(columnNames = {"order_id", "attachment_type"})
}
)
@RowId("ROWID")
@EntityListeners(AuditingEntityListener.class)
public class Attachment extends AuditInfo {

    @ApiModelProperty(notes="Уникальный идентификатор записи (id)", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachment_gen")
    @SequenceGenerator(name = "attachment_gen", sequenceName = "attachment_seq", allocationSize = 1)
    private Long id;

    @ApiModelProperty(notes="Идентификатор заявки", required = true)
    @NotNull(message = "Для вложения обязательно ID заявки")
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id", insertable = false, updatable = false, nullable = false)
    private AbstractOrder order;

    @ApiModelProperty(notes="Тип вложения как ENUM", required = true)
    @NotNull(message = "Тип вложения не может быть пустым")
    @Enumerated(EnumType.STRING)
    @Column(name = "attachment_type", nullable = false, length = 100)
    private AttachmentType attachmentType;

    @ApiModelProperty(notes="Ссылка на тип документа oCRM", required = true)
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "doc_code")),
            @AttributeOverride(name = "text", column = @Column(name = "doc_name")),
            @AttributeOverride(name = "dictName", column = @Column(name = "doc_source")),
            @AttributeOverride(name = "dictLang", column = @Column(name = "doc_name_lang"))
    })
    private CommonServiceRef attachmentTypeRef;

    @ApiModelProperty(notes="Тип контента")
    @Column(name = "content_type")
    private String contentType;

    @ApiModelProperty(notes="Каталог")
    @Column(name = "catalog", nullable = false, length = 4000)
    private String catalog;

    @ApiModelProperty(notes="Наименование файла")
    @Column(name = "file_name", nullable = false, length = 2000)
    private String fileName;

    @ApiModelProperty(notes="Наименование файла")
    @Column(name = "original_file_name", length = 2000)
    private String originalFilename;

    //From Alfresco
    @ApiModelProperty(notes="Ссылка на файл Alfresco")
    @Column(name = "file_node_ref", length = 4000)
    private String fileNodeRef;

    @ApiModelProperty(notes="Расширение файла")
    @Column(name = "file_name_extension", length = 500)
    private String fileNameExtension;


    public Attachment(){}

    public Attachment(@NotNull(message = "Для вложения обязательно ID заявки") Long orderId, @NotNull(message = "Тип вложения не может быть пустым") AttachmentType attachmentType) {
        this.orderId = orderId;
        this.attachmentType = attachmentType;
    }

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

    public AbstractOrder getOrder() {
        return order;
    }

    public void setOrder(AbstractOrder order) {
        this.order = order;
    }

    public AttachmentType getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(AttachmentType attachmentType) {
        this.attachmentType = attachmentType;
    }

    public CommonServiceRef getAttachmentTypeRef() {
        return attachmentTypeRef;
    }

    public void setAttachmentTypeRef(CommonServiceRef attachmentTypeRef) {
        this.attachmentTypeRef = attachmentTypeRef;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getFileNodeRef() {
        return fileNodeRef;
    }

    public void setFileNodeRef(String fileNodeRef) {
        this.fileNodeRef = fileNodeRef;
    }

    public String getFileNameExtension() {
        return fileNameExtension;
    }

    public void setFileNameExtension(String fileNameExtension) {
        this.fileNameExtension = fileNameExtension;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Attachment{");
        sb.append("id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", attachmentType=").append(attachmentType);
        sb.append(", attachmentTypeRef=").append(attachmentTypeRef);
        sb.append(", contentType='").append(contentType).append('\'');
        sb.append(", catalog='").append(catalog).append('\'');
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append(", originalFilename='").append(originalFilename).append('\'');
        sb.append(", fileNodeRef='").append(fileNodeRef).append('\'');
        sb.append(", fileNameExtension='").append(fileNameExtension).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
