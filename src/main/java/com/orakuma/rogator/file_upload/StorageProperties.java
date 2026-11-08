package com.orakuma.rogator.file_upload;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

/**
 * File storage settings ({@code app.storage.*}).
 *
 * <p>{@code root} is bound by Spring to a {@link Path} using the host file system, so Windows
 * (for example drive-letter paths), Linux and macOS ({@code /var/lib/rogator}) values are all accepted.
 */
@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(
    /* Base directory where files are stored. Created on startup if missing. */
    Path root,
    /* Sub-folder (under root) holding application attachments. Single safe path segment. */
    String attachmentsFolderName) {

  public StorageProperties {
    if (attachmentsFolderName == null || attachmentsFolderName.isBlank()) {
      attachmentsFolderName = "applications-attachments";
    }
    if (root == null) {
      throw new IllegalArgumentException("app.storage.root must be configured");
    }
    if (!attachmentsFolderName.matches("[A-Za-z0-9._-]+")) {
      throw new IllegalArgumentException(
          "app.storage.attachments-folder-name must be a single segment of [A-Za-z0-9._-]");
    }
  }
}
