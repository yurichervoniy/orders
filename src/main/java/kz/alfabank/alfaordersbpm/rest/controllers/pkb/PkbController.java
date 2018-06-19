package kz.alfabank.alfaordersbpm.rest.controllers.pkb;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kz.alfabank.alfaordersbpm.domain.models.audit.Auditable;
import kz.alfabank.alfaordersbpm.domain.models.pkb.PkbChecks;
import kz.alfabank.alfaordersbpm.domain.models.pkb.PkbData;
import kz.alfabank.alfaordersbpm.domain.service.pkb.PkbService;
import kz.alfabank.alfaordersbpm.rest.controllers.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin
@RequestMapping(Constants.API_BASE + "/retail/v1/pkb")
public class PkbController {
    private final PkbService pkbService;

    @Autowired
    public PkbController(PkbService pkbService) {
        this.pkbService = pkbService;
    }

    @ApiOperation(value = "Основные данные для отчета ПКБ", notes = "Основные данные для отчета ПКБ")
    @GetMapping(value = "/getdata/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Основные данные для отчета ПКБ")
    @PreAuthorize("hasPermission(#orderId, 'OrderPkbData', 'read')")
    public CompletableFuture<PkbData> getData(
            @ApiParam(value = "ID заявки") @PathVariable Long orderId) {
        return pkbService.getPkbData(orderId);
    }

    @ApiOperation(value = "Проверки для отчета ПКБ", notes = "Проверки для отчета ПКБ")
    @GetMapping(value = "/getchecks/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Проверки для отчета ПКБ")
    @PreAuthorize("hasPermission(#orderId, 'OrderPkbChecks', 'read')")
    public CompletableFuture<List<PkbChecks>> getChecks(
            @ApiParam(value = "ID заявки") @PathVariable Long orderId) {
        return pkbService.getPkbChecks(orderId);
    }

    @ApiOperation(value = "Отчет ПКБ в HTML", notes = "Отчет ПКБ в HTML")
    @GetMapping(value = "/gethtml/{orderId}")
    @Auditable(eventName = "Отчет ПКБ в HTML")
    @PreAuthorize("hasPermission(#orderId, 'OrderPkbReport', 'read')")
    public ResponseEntity<String> getHtml(
            @ApiParam(value = "ID заявки") @PathVariable Long orderId) {
        String report = pkbService.getPkbHtml(orderId);
           return ResponseEntity.ok()
                   .contentType(MediaType.TEXT_HTML)
                   .body(report)
                   ;
    }

    @ApiOperation(value = "Ответ ПКБ в XML", notes = "Ответ ПКБ в XML")
    @GetMapping(value = "/getresponse/{orderId}")
    @Auditable(eventName = "Ответ ПКБ в XML")
    @PreAuthorize("hasPermission(#orderId, 'OrderPkbReport', 'read')")
    public ResponseEntity<Resource> getResponse(
            @ApiParam(value = "ID заявки") @PathVariable Long orderId) throws UnsupportedEncodingException {
        String report = pkbService.getPkbResponse(orderId);
        ByteArrayResource result = new ByteArrayResource(report.getBytes("UTF-8"));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=xml_"+orderId+".xml")
                .contentLength(result.contentLength())
                .contentType(MediaType.APPLICATION_XML)
                .body(result);
    }
}
