package kz.alfabank.alfaordersbpm.domain.models.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(description = "Модель справочников")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictionaryModel {
    @ApiModelProperty(notes="Код в справочнике") private String value;
    @ApiModelProperty(notes="Описание") private String text;
    @ApiModelProperty(notes="Имя справочника") private String dictName;
    @ApiModelProperty(notes="Язык справочника") private String dictLang;

    @JsonCreator
    public DictionaryModel(@JsonProperty("value") String value,
                           @JsonProperty("text") String text,
                           @JsonProperty("dictName") String dictName,
                           @JsonProperty("dictLang") String dictLang) {
        this.value = value;
        this.text = text;
        this.dictName = dictName;
        this.dictLang = dictLang;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictLang() {
        return dictLang;
    }

    public void setDictLang(String dictLang) {
        this.dictLang = dictLang;
    }

    @Override
    public String toString() {
        return "DictionaryModel{" +
                "value='" + value + '\'' +
                ", text='" + text + '\'' +
                ", dictName='" + dictName + '\'' +
                ", dictLang='" + dictLang + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictionaryModel that = (DictionaryModel) o;
        return Objects.equals(value, that.value) &&
                Objects.equals(text, that.text) &&
                Objects.equals(dictName, that.dictName) &&
                Objects.equals(dictLang, that.dictLang);
    }

    @Override
    public int hashCode() {

        return Objects.hash(value, text, dictName, dictLang);
    }
}
