package com.orakuma.rogator.file_upload;

import java.util.List;

public record MultiFileUploadResponse(
        List<UploadedFileDto> files,
        String error
) {
  public MultiFileUploadResponse(List<UploadedFileDto> files) {
    this(files, null);
  }

  public static MultiFileUploadResponse error(String message) {
    return new MultiFileUploadResponse(null, message);
  }
}
