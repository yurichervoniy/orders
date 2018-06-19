package kz.alfabank.alfaordersbpm.domain.service.bpmocrm.commonservice;

import kz.alfabank.alfaordersbpm.config.BpmCrmProperties;
import kz.alfabank.alfaordersbpm.domain.models.exception.CommonServiceEmptyValuesException;
import kz.alfabank.alfaordersbpm.domain.models.exception.CommonServiceException;
import kz.alfabank.alfaordersbpm.domain.models.exception.InternalServerException;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValAttrResponse;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValWeb;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValWebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Service
@Validated
public class CommonServiceProxyImpl implements CommonServiceProxy {

    private static final Logger logger = LoggerFactory.getLogger(CommonServiceProxyImpl.class);
    private static final int CONNECT_TIMEOUT = 4000;
    private static final int READ_TIMEOUT = 10000;

    @NotBlank
    private final String urlDict;
    @NotBlank
    private final String urlAttrs;

    private final RestTemplate restTemplate;

    @Autowired
    public CommonServiceProxyImpl(RestTemplateBuilder restTemplateBuilder, BpmCrmProperties bpmCrmProperties) {
        this.restTemplate = restTemplateBuilder
                            .setConnectTimeout(CONNECT_TIMEOUT)
                            .setReadTimeout(READ_TIMEOUT)
                            .build();
        String url = bpmCrmProperties.getDictionaries().getCommonServiceUrl().toString();
        urlDict = url + "/get_vals_by_dict_name_web/{dictName}";
        urlAttrs = url + "/get_dict_val_attrs/{dict_name}/{dict_code}";
    }

    @Override
    @Async
    public CompletableFuture<DictValWebResponse> getDictionary(String dictName) {
        logger.debug("getDictionary {}", dictName);
        try {
            DictValWebResponse result = restTemplate.getForObject(urlDict, DictValWebResponse.class, dictName);
            if(result.getRetCode() < 0)
                throw new CommonServiceException(String.format("Ошибка получения общего справочника BPMOCRM RetCode [%s]<0 с Название справочник %s retText %s",result.getRetCode(),dictName, result.getRetText()));
            return CompletableFuture.completedFuture(result);
        }
        catch (HttpStatusCodeException se){
            logger.error("getDictionary HttpStatusCodeException", se);
            throw new CommonServiceException(String.format("Ошибка получения общего справочника BPMOCRM HttpStatusCodeException Название справочника=%s status=%s %s body=%s", dictName, se.getStatusCode(), se.getStatusText(), se.getResponseBodyAsString()), se);
        }
        catch (Exception ex){
            logger.error("getDictionary Exception", ex);
            throw new CommonServiceException(String.format("Ошибка получения общего справочника BPMOCRM Название справочника=%s message=%s", dictName, ex.getMessage()), ex);
        }

    }

    @Override
    @Async
    public CompletableFuture<DictValAttrResponse> getDictValAttrs(String dictName, String dictCode) {
        logger.debug("getDictValAttrs {}, for code {}",dictName, dictCode);
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("dict_name", dictName);
        uriVariables.put("dict_code", dictCode);
        try {
            DictValAttrResponse result = restTemplate.getForObject(urlAttrs, DictValAttrResponse.class, uriVariables);
            logger.debug("getDictValAttrs received values {}",result);
            return CompletableFuture.completedFuture(result);
        }
        catch (HttpStatusCodeException se){
            logger.error("getDictValAttrs HttpStatusCodeException", se);
            throw new CommonServiceException(String.format("Ошибка получения атрибутов общего справочника HttpStatusCodeException dictName=%s dictCode=%s status=%s %s body=%s", dictName, dictCode, se.getStatusCode(), se.getStatusText(), se.getResponseBodyAsString()), se);
        }
        catch (Exception ex){
            logger.error("getDictValAttrs Exception", ex);
            throw new CommonServiceException(String.format("Ошибка получения атрибутов общего справочника Exception dictName=%s dictCode=%s message=%s", dictName, dictCode, ex.getMessage()), ex);
        }

    }

    @Override
    @Async
    public CompletableFuture<List<DictValWeb>> getDictValues(String dictName) {
        logger.debug("getDictValues (no httprequest) {}", dictName);
        return  getDictionary(dictName)
                .thenApply(DictValWebResponse::getValWebs);
    }

    @Override
    @Async
    public CompletableFuture<Optional<CommonServiceRef>> getDictValByCode(String dictName, String dictCode) {
        logger.debug("getDictValByCode (no httprequest) {} {}", dictName, dictCode);
        return getDictValues(dictName)
                .thenApply(dictValWebs -> {
                    if(dictValWebs.isEmpty())
                        throw new CommonServiceEmptyValuesException(String.format("Нет значений для справочника %s",dictName));
                    return dictValWebs.stream()
                        .filter(dictValWeb -> dictValWeb.getCode().equals(dictCode))
                        .findAny()
                        .map(e->CommonServiceRef.of(e.getCode(), e.getValue(), dictName))
                        ;
                    })
        ;
    }

    @Override
    public CommonServiceRef getDictValByCodeSync(String dictName, String dictCode) {
        CompletableFuture<Optional<CommonServiceRef>> refFuture = getDictValByCode(dictName, dictCode);
        Optional<CommonServiceRef> result;
        try {
            result = refFuture.get();
        } catch (Exception e) {
            throw new InternalServerException("Ошибка получения общего справочника-> calling get CommonService Result " + e.getMessage(), e);
        }

        return result
                .orElseThrow(()->new CommonServiceException(String.format("Ошибка получения общего справочника значение с кодом [%s] не найдено в справочнике %s", dictCode, dictName)));

    }

}
