package kz.alfabank.alfaordersbpm.rest.controllers.gcvp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kz.alfabank.alfaordersbpm.domain.models.audit.Auditable;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.exception.InternalServerException;
import kz.alfabank.alfaordersbpm.domain.models.gcvp.GcvpReport;
import kz.alfabank.alfaordersbpm.domain.models.gcvp.GcvpReportDetail;
import kz.alfabank.alfaordersbpm.domain.models.gcvp.GcvpRequestResponse;
import kz.alfabank.alfaordersbpm.domain.repositories.gcvp.GcvpReportDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@Api(value = "API для отчетов ГЦВП")
@CrossOrigin
@RequestMapping(value = "api/gcvp/v1")
public class GcvpReportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GcvpReportController.class);
    private static final String EXCEPTION = "GcvpReportControllerException=%s";

    private final GcvpReportDao gcvpReportDao;

    @Autowired
    public GcvpReportController(GcvpReportDao gcvpReportDao) {
        this.gcvpReportDao = gcvpReportDao;
    }

    @ApiOperation(value = "Получить последний ГЦВП отчет по заявке", notes = "Получить последний ГЦВП отчет по заявке", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/reports/orders/{orderId}/last", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Получить последний ГЦВП отчет по заявке")
    @PreAuthorize("hasPermission(#orderId, 'OrderGcvpReport', 'read')")
    public GcvpReport getOrderLastReport(@PathVariable long orderId){
        LOGGER.debug("REST request to getOrderLastReport orderId=[{}]", orderId);
        if (orderId<=0)
            throw new BadRequestException("getOrderLastReport, orderId must be greater than zero");
        try {
            return gcvpReportDao.getOrderLastReport(orderId)
                    .orElseThrow(()->new EntityNotFoundException(String.format("GcvpReport with orderId=%s not found", orderId)));
        } catch (SQLException e) {
            String s = String.format("SQLException getOrderLastReport orderId=%s Message=%s", orderId, e.getMessage());
            LOGGER.error(String.format(EXCEPTION, s, e.toString()), e);
            throw new InternalServerException(s,e);
        }
    }

    @ApiOperation(value = "Получить детали для отчета ГЦВП", notes = "Получить детали для отчета ГЦВП", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/reports/{id}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Получить детали для отчета ГЦВП")
    @PreAuthorize("hasPermission(#id, 'GcvpReportDetails', 'read')")
    public List<GcvpReportDetail> getGcvpReportDetails(@PathVariable long id){
        LOGGER.debug("REST request to getGcvpReportDetails reportId=[{}]", id);
        if (id<=0)
            throw new BadRequestException("getGcvpReportDetails, reportId must be greater than zero");
        try {
            return gcvpReportDao.getReportDetails(id);
        } catch (SQLException e) {
            String s = String.format("SQLException getGcvpReportDetails reportId=%s Message=%s", id, e.getMessage());
            LOGGER.error(String.format(EXCEPTION, s, e.toString()), e);
            throw new InternalServerException(s,e);
        }
    }

    @ApiOperation(value = "Получить инфо по запросу ГЦВП", notes = "Получить инфо по запросу ГЦВП", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/requests/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Получить инфо по запросу ГЦВП")
    @PreAuthorize("hasPermission(#id, 'GcvpRequestResponse', 'read')")
    public GcvpRequestResponse getGcvpRequestInfo(@PathVariable long id){
        LOGGER.debug("REST request to getGcvpRequestInfo id=[{}]", id);
        if (id<=0)
            throw new BadRequestException("getGcvpRequestInfo, id must be greater than zero");
        try {
            return gcvpReportDao.getGcvpRqRsInfo(id).orElseThrow(()->new EntityNotFoundException(String.format("GcvpRequestResponse with id=%s not found", id)));
        } catch (SQLException e) {
            String s = String.format("SQLException getGcvpRequestInfo id=%s Message=%s", id, e.getMessage());
            LOGGER.error(String.format(EXCEPTION, s, e.toString()), e);
            throw new InternalServerException(s,e);
        }
    }

}
