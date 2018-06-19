package kz.alfabank.alfaordersbpm.domain.models.ocrmrefs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DictValWebResponse {

    @JsonProperty(value = "RETCODE")
    private Integer retCode;

    @JsonProperty(value = "RETTEXT")
    private String retText;

    @JsonProperty(value = "RESPONSE")
    private List<DictValWeb> valWebs;

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public List<DictValWeb> getValWebs() {
        return valWebs;
    }

    public void setValWebs(List<DictValWeb> valWebs) {
        this.valWebs = valWebs;
    }

    public String getRetText() {
        return retText;
    }

    public void setRetText(String retText) {
        this.retText = retText;
    }

    @Override
    public String toString() {
        return "DictValWebResponse{" +
                "retCode=" + retCode +
                ", valWebs=" + valWebs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictValWebResponse that = (DictValWebResponse) o;
        return Objects.equals(retCode, that.retCode) &&
                Objects.equals(valWebs, that.valWebs);
    }

    @Override
    public int hashCode() {

        return Objects.hash(retCode, valWebs);
    }
}
