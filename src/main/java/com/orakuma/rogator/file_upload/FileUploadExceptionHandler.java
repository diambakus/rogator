package com.orakuma.rogator.file_upload;

import com.orakuma.rogator.config.exceptions.ApiError;
import com.orakuma.rogator.file_upload.exceptions.FileUploadNotFoundException;
import com.orakuma.rogator.file_upload.exceptions.InvalidFileException;
import com.orakuma.rogator.file_upload.exceptions.StorageException;
import com.orakuma.rogator.file_upload.exceptions.StorageFileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = FileUploadController.class)
@Slf4j
public class FileUploadExceptionHandler {

  @ExceptionHandler({StorageFileNotFoundException.class, FileUploadNotFoundException.class})
  ResponseEntity<ApiError> notFound(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError("File not found"));
  }

  @ExceptionHandler(InvalidFileException.class)
  ResponseEntity<ApiError> invalid(InvalidFileException ex) {
    HttpStatus status =
        ex.isUnsupportedType() ? HttpStatus.UNSUPPORTED_MEDIA_TYPE : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(new ApiError(ex.getMessage()));
  }

  @ExceptionHandler(StorageException.class)
  ResponseEntity<ApiError> storage(StorageException ex) {
    log.error("Storage failure", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiError("File storage error"));
  }
}
