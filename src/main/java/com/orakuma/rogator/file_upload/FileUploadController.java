package com.orakuma.rogator.file_upload;

import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("file")
@RequiredArgsConstructor
public class FileUploadController {

  private final FileUploadService fileUploadService;

  @PostMapping("/uploadMultipleFiles/{applicationId}")
  public ResponseEntity<List<UploadedFileDto>> upload(
      @PathVariable Long applicationId, @RequestParam("file") List<MultipartFile> files) {
    if (files.isEmpty()) {
      return ResponseEntity.badRequest().body(List.of(new UploadedFileDto("Files list is empty!")));
    }
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(withDownloadUrls(fileUploadService.storeAll(applicationId, files)));
  }

  @GetMapping("/downloadApplicationFiles/{applicationId}")
  public ResponseEntity<List<UploadedFileDto>> listApplicationFiles(
      @PathVariable Long applicationId) {
    return ResponseEntity.ok(withDownloadUrls(fileUploadService.findByApplicationId(applicationId)));
  }

  @GetMapping("/files/{id}")
  public ResponseEntity<Resource> download(@PathVariable Long id) {
    FileDownload file = fileUploadService.download(id);

    MediaType mediaType =
        file.contentType() == null
            ? MediaType.APPLICATION_OCTET_STREAM
            : MediaType.parseMediaType(file.contentType());

    ResponseEntity.BodyBuilder response =
        ResponseEntity.ok()
            .contentType(mediaType)
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                // RFC 6266/5987 encoding: safe for quotes, CR/LF and non-ASCII names.
                ContentDisposition.attachment()
                    .filename(file.filename(), StandardCharsets.UTF_8)
                    .build()
                    .toString())
            .header("X-Content-Type-Options", "nosniff")
            .cacheControl(CacheControl.noStore());
    if (file.size() != null) {
      response.contentLength(file.size());
    }
    return response.body(file.resource());
  }

  /** The {@code filePath} field of the response carries the download URL, never the disk path. */
  private List<UploadedFileDto> withDownloadUrls(List<UploadedFileDto> files) {
    return files.stream()
        .map(
            f ->
                new UploadedFileDto(
                    f.id(),
                    f.filename(),
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/file/files/{id}")
                        .buildAndExpand(f.id())
                        .toUriString(),
                    null))
        .toList();
  }
}
