package kz.alfabank.alfaordersbpm.rest.controllers.blacklist;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kz.alfabank.alfaordersbpm.domain.models.audit.Auditable;
import kz.alfabank.alfaordersbpm.domain.models.blacklist.BLCheckResults;
import kz.alfabank.alfaordersbpm.domain.models.blacklist.BLRequestResponse;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.InternalServerException;
import kz.alfabank.alfaordersbpm.domain.repositories.blacklist.BlackListDAO;
import kz.alfabank.alfaordersbpm.rest.controllers.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Api(value = "API для получения результатов проверок на черный список")
@CrossOrigin
@RestController
@RequestMapping(Constants.API_BASE + "/retail/v1/blacklist/reposrts")
public class BlackListController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlackListController.class);

    private final BlackListDAO blackListDAO;

    @Autowired
    public BlackListController(BlackListDAO blackListDAO) {
        this.blackListDAO = blackListDAO;
    }

    @ApiOperation(value = "Получить XML запроса и ответа проверок на ЧС", notes = "Получить XML запроса и ответа проверок на ЧС", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping("/{orderId}/xml")
    @Auditable(eventName = "Получение запроса/ответа проверки на ЧС")
    @PreAuthorize("hasPermission(#orderId, 'OrderBlackList', 'read')")
    public List<BLRequestResponse> getBLRequestResponse(@PathVariable Long orderId){
        LOGGER.debug("REST request to getBLRequestResponse orderId=[{}]", orderId);
        if (orderId<=0)
            throw new BadRequestException("getBLRequestResponse, reportId must be greater than zero");
        try {
            return blackListDAO.getBLRqRsInfo(orderId);
        } catch (SQLException e) {
            String s = String.format("SQLException getBLRequestResponse orderId=%s Message=%s", orderId, e.getMessage());
            LOGGER.error(String.format("%s Exception=%s", s, e.toString()), e);
            throw new InternalServerException(s,e);
        }
    }

    @ApiOperation(value = "Получить результаты проверки на черный список", notes = "Получить результаты проверки на черный список", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping("/{orderId}")
    @Auditable(eventName = "Получение результатов проверки на ЧС")
    @PreAuthorize("hasPermission(#orderId, 'OrderBlackList', 'read')")
    public List<BLCheckResults> getBLResultBLChecks(@PathVariable Long orderId){
        LOGGER.debug("REST request to getBLResultBLChecks orderId=[{}]", orderId);
        if (orderId<=0)
            throw new BadRequestException("getBLResultBLChecks, reportId must be greater than zero");
        try {
            return blackListDAO.getResultsChecks(orderId);
        } catch (SQLException e) {
            String s = String.format("SQLException getBLResultBLChecks orderId=%s Message=%s", orderId, e.getMessage());
            LOGGER.error(String.format("%s Exception=%s", s, e.toString()), e);
            throw new InternalServerException(s,e);
        }
    }
}
