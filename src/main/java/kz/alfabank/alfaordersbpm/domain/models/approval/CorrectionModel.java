package kz.alfabank.alfaordersbpm.domain.models.approval;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Поля, прошедшие проверку")
public class CorrectionModel {
    @ApiModelProperty(notes="Наименование поля")
    private String fieldName;
    @ApiModelProperty(notes="Старое значение")
    private Object oldValue;
    @ApiModelProperty(notes="Новое значение")
    private Object newValue;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    @Override
    public String toString() {
        return "CorrectionModel{" +
                "fieldName='" + fieldName + '\'' +
                ", oldValue=" + oldValue +
                ", newValue=" + newValue +
                '}';
    }
}
