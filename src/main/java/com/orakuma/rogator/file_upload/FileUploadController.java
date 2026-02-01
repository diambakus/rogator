package com.orakuma.rogator.file_upload;

import com.orakuma.rogator.file_upload.exceptions.StorageFileNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("file")
@AllArgsConstructor
public class FileUploadController {

  private final FileUploadService fileUploadService;
  private final StorageService storageService;

  @PostMapping("/uploadMultipleFiles/{applicationId}")
  public ResponseEntity<List<UploadedFileDto>> handleFileUpload(
      @PathVariable("applicationId") Long applicationId,
      @RequestParam("file") LinkedList<MultipartFile> multipartFiles,
      Authentication authentication) {

    if (multipartFiles.isEmpty()) {
      return ResponseEntity.badRequest().body(List.of(new UploadedFileDto("Files list is empty!")));
    }

    LinkedList<UploadedFileDto> storedFiles =
        new LinkedList<>(fileUploadService.storeAll(applicationId, multipartFiles));

    LinkedHashMap<Long, URI> downloadableURIAndIdMap = buildDownloadableUriAndId(storedFiles);
    LinkedList<UploadedFileDto> responseFilesDto =
        buildResponseDto(storedFiles, downloadableURIAndIdMap);

    return ResponseEntity.status(HttpStatus.CREATED).body(responseFilesDto);
  }

  @GetMapping("/downloadApplicationFiles/{applicationId}")
  private ResponseEntity<List<UploadedFileDto>> getUploadedFiles(
      @PathVariable("applicationId") Long applicationId, Authentication authentication) {
    LinkedList<UploadedFileDto> uploadedFileDtos =
        new LinkedList<>(fileUploadService.findByApplicationId(applicationId));
    LinkedHashMap<Long, URI> downloadableURIAndIdMap = buildDownloadableUriAndId(uploadedFileDtos);
    LinkedList<UploadedFileDto> responseFilesDto =
        buildResponseDto(uploadedFileDtos, downloadableURIAndIdMap);
    return ResponseEntity.status(HttpStatus.OK).body(responseFilesDto);
  }

  @GetMapping("/files/{id}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable("id") Long id) {

    UploadedFileDto uploadedFileDto = fileUploadService.findById(id);
    Resource file = storageService.loadAsResource(uploadedFileDto.filePath());

    if (file == null) return ResponseEntity.notFound().build();

    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
        .body(file);
  }

  @ExceptionHandler(StorageFileNotFoundException.class)
  public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
    return ResponseEntity.notFound().build();
  }

  private LinkedHashMap<Long, URI> buildDownloadableUriAndId(LinkedList<UploadedFileDto> files) {
    return files.stream()
        .collect(
            Collectors.toMap(
                UploadedFileDto::id,
                fileUploadDto ->
                    MvcUriComponentsBuilder.fromMethodName(
                            FileUploadController.class, "serveFile", fileUploadDto.id())
                        .build()
                        .toUri(),
                (existing, replacement) -> existing,
                LinkedHashMap::new));
  }

  private LinkedList<UploadedFileDto> buildResponseDto(
      LinkedList<UploadedFileDto> uploadedFileDtos,
      LinkedHashMap<Long, URI> downloadableURIAndIdMap) {
    LinkedList<UploadedFileDto> responseDtos = new LinkedList<>();
    uploadedFileDtos.forEach(
        fileUploadDto -> {
          responseDtos.add(
              new UploadedFileDto(
                  fileUploadDto.id(),
                  fileUploadDto.filename(),
                  String.valueOf(downloadableURIAndIdMap.get(fileUploadDto.id())),
                  null));
        });
    return responseDtos;
  }
}
