package com.orakuma.rogator.file_upload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@Setter
@Getter
@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {

  /** Folder location for storing files */
  private Path root;

  private String attachmentsFolderName;
}
