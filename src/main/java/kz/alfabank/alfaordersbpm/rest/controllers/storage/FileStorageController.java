package kz.alfabank.alfaordersbpm.rest.controllers.storage;

import io.swagger.annotations.*;
import kz.alfabank.alfaordersbpm.domain.models.attachment.Attachment;
import kz.alfabank.alfaordersbpm.domain.models.attachment.AttachmentType;
import kz.alfabank.alfaordersbpm.domain.models.audit.Auditable;
import kz.alfabank.alfaordersbpm.domain.models.dto.StorageContent;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.rest.controllers.Constants;
import kz.alfabank.alfaordersbpm.domain.service.storage.StorageService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Api(value = "API для загрузки файлов")
@CrossOrigin
@RestController
@RequestMapping(Constants.API_BASE + "/retail/v1/attachments")
public class FileStorageController {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageController.class);

    private final StorageService storageService;

    @Autowired
    public FileStorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @ApiOperation(value = "Получить список доступных типов документов", notes = "Получить список доступных типов документов")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping("/availabletypes")
    @Auditable(eventName = "Получить список доступных типов документов")
    public List<AttachmentType> getAttachmentTypes(){
        return Arrays.asList(AttachmentType.values());
    }

    @ApiOperation(value = "Получить документ по ИД", notes = "Получить вложение по ИД")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping("/{id}")
    @Auditable(eventName = "Получить Attachmen по ИД")
    @PreAuthorize("hasPermission(#id, 'Attachment', 'read')")
    public ResponseEntity<Attachment> getAttachmentById(@ApiParam(value = "ИД вложения", required = true) @PathVariable Long id){
        logger.debug("REST getAttachmentById for AttachmentId[{}]", id);
        if (id == null)
            throw new BadRequestException("To get Attachment, Id must not be null");
        Attachment attachment = storageService.getOne(id)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Attachment with ID[%s] not found", id)));
        return ResponseEntity.ok(attachment);
    }

    @ApiOperation(value = "Загрузить файл документа", notes = "Загрузить документ вложения")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping("/{id}/download")
    @Auditable(eventName = "Загрузить файл документа")
    @PreAuthorize("hasPermission(#id, 'Attachment', 'download')")
    public ResponseEntity<Resource> downloadFile(@ApiParam(value = "ИД вложения", required = true) @PathVariable Long id){
        logger.debug("REST downloadFile for AttachmentId[{}]", id);
        if (id == null)
            throw new BadRequestException("To download file AttachmentId must not be null");
        StorageContent content = storageService.getFileContent(id);
        ByteArrayResource resource = new ByteArrayResource(content.getBytes(), "resource loaded from Storage");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment;filename=%s",content.getFileName()))
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType(content.getContentType().orElse("application/octet-stream")))
                .body(resource);
    }

    @ApiOperation(value = "Получить документ заявки", notes = "Получить документ заявки")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping("/order/{orderId}")
    @Auditable(eventName = "Получить Attachments заявки")
    @PreAuthorize("hasPermission(#orderId, 'OrderAttachment', 'read')")
    public ResponseEntity<Attachment> getOrderAttachment(@ApiParam(value = "ИД заявки", required = true) @PathVariable Long orderId,
                                                         @ApiParam(value = "Тип файла", required = true) @RequestParam("attachmentType") AttachmentType attachmentType){
        logger.debug("REST getOrderAttachment orderId=[{}] attachmentType=[{}]", orderId, attachmentType);
        if (orderId == null)
            throw new BadRequestException("To get Attachment, orderId must not be null");
        if (attachmentType == null)
            throw new BadRequestException("To get Attachment, attachmentType must not be null");

        Attachment attachment = storageService.getOrderAttachment(orderId, attachmentType)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Attachment with ID[%s] and AttachmentType[%s] not found", orderId, attachmentType)));
        return ResponseEntity.ok(attachment);
    }

    @ApiOperation(value = "Загрузить файл документа заявки", notes = "Загрузить файл документа заявки")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @GetMapping("/order/{orderId}/download")
    @Auditable(eventName = "Загрузить файл документа заявки")
    @PreAuthorize("hasPermission(#orderId, 'OrderAttachment', 'download')")
    public ResponseEntity<Resource> downloadOrderFile(@ApiParam(value = "ИД заявки", required = true) @PathVariable Long orderId,
                                                      @ApiParam(value = "Тип файла", required = true) @RequestParam("attachmentType") AttachmentType attachmentType){
        logger.debug("REST getOrderAttachment orderId=[{}] attachmentType=[{}]", orderId, attachmentType);
        if (orderId == null)
            throw new BadRequestException("To get Attachment, orderId must not be null");
        if (attachmentType == null)
            throw new BadRequestException("To get Attachment, attachmentType must not be null");
        StorageContent content = storageService.getOrderFileContent(orderId, attachmentType);
        ByteArrayResource resource = new ByteArrayResource(content.getBytes(), "resource loaded from Storage");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment;filename=%s",content.getFileName()))
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType(content.getContentType().orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE)))
                .body(resource);
    }

    @ApiOperation(value = "Upload документа заявки", notes = "Upload документа заявки")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PostMapping("/order/{orderId}/upload")
    @Auditable(eventName = "Upload документа заявки")
    @PreAuthorize("hasPermission(#orderId, 'OrderAttachment', 'upload')")
    public ResponseEntity<Attachment> uploadOrderFile(
            @ApiParam(value = "ИД заявки", required = true) @PathVariable Long orderId,
            @ApiParam(value = "Тип загружаемого файла", required = true) @RequestParam("attachmentType") AttachmentType attachmentType,
            @ApiParam(value = "Файл", required = true) @RequestParam("file") MultipartFile uploadfile) throws IOException {

        logger.debug("REST uploadOrderFile orderId=[{}] attachmentType=[{}] originalFileName=[{}]", orderId, attachmentType, uploadfile.getOriginalFilename());
        if (orderId == null)
            throw new BadRequestException("To uploadOrderFile orderId must not be null");
        if (attachmentType == null)
            throw new BadRequestException("To uploadOrderFile attachmentType must not be null");
        if (uploadfile.isEmpty()) {
            throw new BadRequestException("uploadOrderFile MultipartFile is empty");
        }

        Attachment attachment = storageService.storeOrderFile(orderId, attachmentType, uploadfile);
        return ResponseEntity.ok(attachment);

    }

    @PostMapping("/order/{orderId}/PASSPORT_PHOTO")
    @Auditable(eventName = "Завершение загрузки документа, удостоверяющего личность")
    @PreAuthorize("hasPermission(#orderId, 'OrderAttachment', 'upload')")
    public void passportPhotoComplete(@ApiParam(value = "ИД заявки", required = true) @PathVariable Long orderId) {
        logger.debug("REST passportPhotoComplete orderId=[{}]", orderId);
        if (orderId == null)
            throw new BadRequestException("To uploadOrderFile orderId must not be null");
        storageService.completePassportPhoto(orderId);
    }


}
