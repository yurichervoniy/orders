package kz.alfabank.alfaordersbpm.domain.models.ocrmrefs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DictValAttrResponse {

    @JsonProperty(value = "RETCODE")
    private Integer retCode;

    @JsonProperty(value = "RESPONSE")
    private List<DictValAttr> valAttrs;

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public List<DictValAttr> getValAttrs() {
        return valAttrs;
    }

    public void setValAttrs(List<DictValAttr> valAttrs) {
        this.valAttrs = valAttrs;
    }

    @Override
    public String toString() {
        return "DictValAttrResponse{" +
                "retCode=" + retCode +
                ", valAttrs=" + valAttrs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictValAttrResponse that = (DictValAttrResponse) o;
        return Objects.equals(retCode, that.retCode) &&
                Objects.equals(valAttrs, that.valAttrs);
    }

    @Override
    public int hashCode() {

        return Objects.hash(retCode, valAttrs);
    }
}
