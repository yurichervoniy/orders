package kz.alfabank.alfaordersbpm.domain.models.ocrmrefs;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@ApiModel(description = "Основные поля универсального справочника BPMoCRM")
@Embeddable
public class CommonServiceRef {

    private static final String DEFAULT_LANG = "ru";

    @ApiModelProperty(notes="Код записи", required = true)
    @NotBlank(message = "Код записи не может быть пустым")
    @Column(name = "d_code", nullable = false)
    private String value;

    @ApiModelProperty(notes="Значение", required = true)
    @NotBlank(message = "Значение не может быть пустым")
    @Column(name = "d_value", nullable = false)
    private String text;

    @ApiModelProperty(notes="Код справочника", required = true)
    @NotBlank(message = "Код справочника не может быть пустым")
    @Column(name = "dict_name", nullable = false)
    private String dictName;

    @ApiModelProperty(notes="Код языка")
    @NotBlank(message = "Код языка не может быть пустым")
    @Column(name = "dict_lang", nullable = false)
    private String dictLang = DEFAULT_LANG;

    CommonServiceRef(){}

    private CommonServiceRef(String value, String text, String dictName){
        this.value = value;
        this.text = text;
        this.dictName = dictName;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public String getDictName() {
        return dictName;
    }

    public String getDictLang() {
        return dictLang;
    }

    public static CommonServiceRef of(String code, String value, String dictName){
        return new CommonServiceRef(code,value,dictName);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public void setDictLang(String dictLang) {
        this.dictLang = dictLang;
    }

    public static String getDefaultLang(){
        return DEFAULT_LANG;
    }

    @Override
    public String toString() {
        return "CommonServiceRef{" +
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
        CommonServiceRef that = (CommonServiceRef) o;
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
