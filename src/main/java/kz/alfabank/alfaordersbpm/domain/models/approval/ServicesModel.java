package kz.alfabank.alfaordersbpm.domain.models.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Модель решения служб")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServicesModel {
    @ApiModelProperty(notes="Описание решения") private String text;
    @ApiModelProperty(notes="Код решения") private String value;
    @ApiModelProperty(notes="Код в справочнике") private String dictName;

    @JsonCreator
    public ServicesModel(@JsonProperty("text") String text,
                         @JsonProperty("value") String value,
                         @JsonProperty("dictName") String dictName) {
        this.text = text;
        this.value = value;
        this.dictName = dictName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    @Override
    public String toString() {
        return "ServicesModel{" +
                "text='" + text + '\'' +
                ", value='" + value + '\'' +
                ", dictName='" + dictName + '\'' +
                '}';
    }
}
