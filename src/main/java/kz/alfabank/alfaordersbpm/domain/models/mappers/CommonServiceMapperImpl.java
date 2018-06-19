package kz.alfabank.alfaordersbpm.domain.models.mappers;

import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValAttr;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValAttrResponse;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValWeb;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValWebResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommonServiceMapperImpl implements CommonServiceMapper {
    @Override
    public Map<String, String> convertDictValsToMap(DictValWebResponse response) {
        Map<String, String> map = new HashMap<>(response.getValWebs().size());
        for(DictValWeb val : response.getValWebs())
            map.put(val.getCode(), val.getValue());
        return map;
    }

    @Override
    public Map<String, String> convertDictAttrsToMap(DictValAttrResponse response) {
        Map<String, String> map = new HashMap<>(response.getValAttrs().size());
        for(DictValAttr val : response.getValAttrs())
            map.put(val.getAttributeName(), val.getAttributeValue());
        return map;
    }
}
