package kz.alfabank.alfaordersbpm.domain.models.mappers;

import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValAttrResponse;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValWebResponse;

import java.util.Map;

public interface CommonServiceMapper {

    Map<String, String> convertDictValsToMap(DictValWebResponse response);

    Map<String, String> convertDictAttrsToMap(DictValAttrResponse response);

}
