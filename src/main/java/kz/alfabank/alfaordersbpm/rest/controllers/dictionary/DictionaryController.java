package kz.alfabank.alfaordersbpm.rest.controllers.dictionary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kz.alfabank.alfaordersbpm.domain.models.audit.Auditable;
import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceCheckType;
import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceDecisionType;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValWebResponse;
import kz.alfabank.alfaordersbpm.rest.controllers.Constants;
import kz.alfabank.alfaordersbpm.domain.service.dictionary.CheckService;
import kz.alfabank.alfaordersbpm.domain.service.dictionary.DecisionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "API справочников")
@CrossOrigin
@RestController
@RequestMapping(Constants.API_BASE + "/retail/v1/dictionary")
@Validated
public class DictionaryController {

    private static final Logger LOG = LoggerFactory.getLogger(DictionaryController.class);
    private static final String DEFAULT_PRODUCE_MEDIA_TYPE = MediaType.APPLICATION_JSON_VALUE;

    private final DecisionService decisionService;
    private final CheckService checkService;

    @Autowired
    public DictionaryController(DecisionService decisionService, CheckService checkService) {
        this.decisionService = decisionService;
        this.checkService = checkService;
    }

    @ApiOperation(value = "Получить значения решений верификаторов", notes = "Получить значения решений верификаторов")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/virifierdecision", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получить значения решений верификаторов")
    public DictValWebResponse getVerifierDecision(){
        LOG.debug("REST getVerifierDecision");
        return decisionService.getServiceDecisionByType(ServiceDecisionType.VERIFIER);
    }

    @ApiOperation(value = "Получить значения решений СБ", notes = "Получить значения решений СБ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/securitydecision", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получить значения решений СБ")
    public DictValWebResponse getSecurityDecision(){
        LOG.debug("REST getSecurityDecision");
        return decisionService.getServiceDecisionByType(ServiceDecisionType.SECURITY);
    }

    @ApiOperation(value = "Получить значения решений КА", notes = "Получить значения решений КА")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/admindecision", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получить значения решений КА")
    public DictValWebResponse getAdminDecision(){
        LOG.debug("REST getAdminDecision");
        return decisionService.getServiceDecisionByType(ServiceDecisionType.CREDITADMIN);
    }

    @ApiOperation(value = "Получить значения решений УРР", notes = "Получить значения решений УРР")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/riskdecision", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получить значения решений УРР")
    public DictValWebResponse getRiskDecision(){
        LOG.debug("REST getRiskDecision");
        return decisionService.getServiceDecisionByType(ServiceDecisionType.RISK);
    }

    @ApiOperation(value = "Получить результат проверки на дополнительную информацию", notes = "Получить результат проверки на дополнительную информацию")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/addinfocheck", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получить результат проверки на дополнительную информацию")
    public DictValWebResponse getAdditionalInfoCheck(){
        LOG.debug("REST getAdditionalInfoCheck");
        return checkService.getServiceCheckByType(ServiceCheckType.ADDINFO);
    }

    @ApiOperation(value = "Получить результат проверки на наличие негативной информации", notes = "Получить результат проверки на наличие негативной информации")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/negativeinfocheck", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получить результат проверки на наличие негативной информации")
    public DictValWebResponse getNegativeInfoCheck(){
        LOG.debug("REST getNegativeInfoCheck");
        return checkService.getServiceCheckByType(ServiceCheckType.NEGATIVEINFO);
    }

    @ApiOperation(value = "Получить результат проверки документа, удостоверяющего личность", notes = "Получить результат проверки документа, удостоверяющего личность")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/idncardcheck", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получить результат проверки документа, удостоверяющего личность")
    public DictValWebResponse getIdnCardCheck(){
        LOG.debug("REST getIdnCardCheck");
        return checkService.getServiceCheckByType(ServiceCheckType.IDNDOC);
    }

    @ApiOperation(value = "Получить результат проверки по базам судебных исполнителей", notes = "Получить результат проверки по базам судебных исполнителей")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/judgecheck", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получить результат проверки по базам судебных исполнителей")
    public DictValWebResponse getJudgeBaseCheck(){
        LOG.debug("REST getJudgeBaseCheck");
        return checkService.getServiceCheckByType(ServiceCheckType.JUDGE);
    }

    @ApiOperation(value = "Получить результат проверки по данным адресов", notes = "Получить результат проверки по данным адресов")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/addresscheck", produces = DEFAULT_PRODUCE_MEDIA_TYPE)
    @Auditable(eventName = "Получить результат проверки по данным адресов")
    public DictValWebResponse getAddressCheck(){
        LOG.debug("REST getAddressCheck");
        return checkService.getServiceCheckByType(ServiceCheckType.ADDRESS);
    }
}
