package kz.alfabank.alfaordersbpm.domain.service.bpmocrm.commonservice;

import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValAttrResponse;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValWeb;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValWebResponse;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface CommonServiceProxy {

    CompletableFuture<DictValWebResponse> getDictionary(String dictName);

    CompletableFuture<DictValAttrResponse> getDictValAttrs(String dictName, String dictCode);

    CompletableFuture<List<DictValWeb>> getDictValues(String dictName);

    CompletableFuture<Optional<CommonServiceRef>> getDictValByCode(String dictName, String dictCode);

    CommonServiceRef getDictValByCodeSync(String dictName, String dictCode);

}
