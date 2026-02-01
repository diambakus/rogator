package com.orakuma.rogator.file_upload;

import com.orakuma.rogator.application.ApplicationEntity;
import com.orakuma.rogator.utils.FilePartsUtils;
import com.orakuma.rogator.utils.RepositoriesHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

  private final FileUploadRepository fileUploadRepository;
  private final FileUploadMapper fileUploadMapper;
  private final RepositoriesHandler repositoriesHandler;
  private final StorageProperties storageProperties;
  private Path rootLocation;

  @PostConstruct
  void init() throws IOException {
    this.rootLocation = storageProperties.getRoot().toAbsolutePath().normalize();
  }

  @Override
  public UploadedFileDto findById(Long id) {
    FileUploadEntity fileUploadEntity = repositoriesHandler.getFileUploadEntityById(id);
    return fileUploadMapper.toDto(fileUploadEntity);
  }

  @Transactional
  @Override
  public List<UploadedFileDto> storeAll(Long applicationId, List<MultipartFile> files) {

    ApplicationEntity applicationEntity =
        repositoriesHandler.getApplicationEntityById(applicationId);

    // 1. Validate everything first
    files.forEach(FilePartsUtils::validateFile);

    LinkedList<FileUploadEntity> persistingEntity = new LinkedList<>();
    Iterable<FileUploadEntity> persistedEntity;
    List<Path> writtenFiles = new ArrayList<>(files.size());

    try {
      for (MultipartFile file : files) {

        String extension = FilePartsUtils.resolveExtension(file.getContentType());
        String storedFilename = UUID.randomUUID() + extension;
        Path relativePath =
            Path.of(
                storageProperties.getAttachmentsFolderName(),
                applicationId.toString(),
                storedFilename);
        Path target = rootLocation.resolve(relativePath).normalize();
        Files.createDirectories(target.getParent());

        // 2. Write file to disk
        try (InputStream in = file.getInputStream()) {
          Files.copy(in, target);
        }

        writtenFiles.add(target);

        FileUploadEntity entity =
            new FileUploadEntity()
                .setFilePath(relativePath.toString())
                .setFilename(file.getOriginalFilename())
                .setFileSize(file.getSize())
                .setFileType(extension)
                .setApplication(applicationEntity)
                .setCreated(LocalDate.now());

        persistingEntity.add(entity);
      }

      persistedEntity = fileUploadRepository.saveAll(persistingEntity);
      return fileUploadMapper.toDtos(
          StreamSupport.stream(persistedEntity.spliterator(), false)
              .collect(Collectors.toCollection(LinkedList::new)));
    } catch (Exception ex) {
      writtenFiles.forEach(this::quietlyDelete);
      log.info("Cleanup filesystem some file(s) fails: {}", ex.getMessage());
      try {
        throw ex;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public List<UploadedFileDto> findByApplicationId(Long applicationId) {
    log.info("Finding files by application id {}", applicationId);
    List<FileUploadEntity> fileUploadEntities = fileUploadRepository.findByApplicationId(applicationId);
    return fileUploadMapper.toDtos(fileUploadEntities);
  }

  private void quietlyDelete(Path path) {
    try {
      Path normalized = path.normalize();
      if (!normalized.startsWith(rootLocation)) {
        log.error("Refusing to delete path outside root: {}", normalized);
        return;
      }
      Files.deleteIfExists(normalized);
    } catch (IOException ex) {
      log.warn("Failed to cleanup file {}", path, ex);
    }
  }
}