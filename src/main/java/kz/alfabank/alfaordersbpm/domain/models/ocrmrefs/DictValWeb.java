package kz.alfabank.alfaordersbpm.domain.models.ocrmrefs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DictValWeb {

    @JsonProperty(value = "D_VALUE")
    private String value;

    @JsonProperty(value = "D_CODE")
    private String code;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictValWeb dictValWeb = (DictValWeb) o;
        return Objects.equals(value, dictValWeb.value) &&
                Objects.equals(code, dictValWeb.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, code);
    }

    @Override
    public String toString() {
        return "dictValWeb{" +
                "value='" + value + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
