package kz.alfabank.alfaordersbpm.rest.controllers.scoring;

import io.swagger.annotations.*;
import kz.alfabank.alfaordersbpm.domain.models.audit.Auditable;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.scoring.ScoringInfo;
import kz.alfabank.alfaordersbpm.domain.repositories.ScoringRepository;
import kz.alfabank.alfaordersbpm.rest.controllers.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "API телефонов по заявкам")
@CrossOrigin
@RestController
@RequestMapping(Constants.API_BASE + "/retail/v1/scoring")
public class ScoringController {

    private static final Logger LOG = LoggerFactory.getLogger(ScoringController.class);

    private final ScoringRepository scoringRepository;

    @Autowired
    public ScoringController(ScoringRepository scoringRepository) {
        this.scoringRepository = scoringRepository;
    }

    @ApiOperation(value = "Скорингы заявки", notes = "Скорингы заявки")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Скорингы заявки")
    @PreAuthorize("hasPermission(#orderId, 'OrderScoring', 'read')")
    public ResponseEntity<List<ScoringInfo>> getOrderScorings(@ApiParam(value = "ИД заявки", required = true) @RequestParam("orderId") Long orderId) {
        LOG.debug("REST request to get getOrderScorings OrderId={}", orderId);
        if (orderId == null)
            throw new BadRequestException("To getOrderScorings orderId must not be null");

        return ResponseEntity.ok(scoringRepository.findAllByOrderIdAndActiveTrueOrderById(orderId));
    }

    @ApiOperation(value = "Загрузить результаты скоринга", notes = "Загрузить результаты скоринга")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping(value = "/{id}/download/{direction}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Auditable(eventName = "Загрузка файла скоринга")
    @PreAuthorize("hasPermission(#id, 'Scoring', 'download')")
    public ResponseEntity<Resource> downloadScoringFile(@ApiParam(value = "ИД скоринга", required = true) @PathVariable Long id
    ,@ApiParam(value = "Тип файла", required = true) @PathVariable ScoringDirection direction) {
        LOG.debug("REST request to downloadScoringFile id={} direction={}", id, direction);
        if (id == null)
            throw new BadRequestException("To getOrderScorings orderId must not be null");

        byte[] data =  ScoringDirection.OUT == direction ? scoringRepository.getResponseBlob(id) : scoringRepository.getRequestBlob(id);
        if (data == null || data.length == 0)
            throw new EntityNotFoundException(String.format("Scoring blob data with id=%d and type=%s is null or empty", id, direction));
        String fileName = String.format("%d_%s.zip", id, direction);
        ByteArrayResource resource = new ByteArrayResource(data, fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment;filename=%s",fileName))
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
