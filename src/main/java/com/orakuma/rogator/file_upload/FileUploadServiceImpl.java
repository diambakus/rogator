package com.orakuma.rogator.file_upload;

import com.orakuma.rogator.application.ApplicationEntity;
import com.orakuma.rogator.file_upload.FileTypeDetector.DetectedType;
import com.orakuma.rogator.utils.RepositoriesHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import com.orakuma.rogator.file_upload.exceptions.FileUploadNotFoundException;
import com.orakuma.rogator.file_upload.exceptions.StorageException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

  private static final int MAX_DISPLAY_NAME_LENGTH = 255;

  private final FileUploadRepository fileUploadRepository;
  private final FileUploadMapper fileUploadMapper;
  private final RepositoriesHandler repositoriesHandler;
  private final StorageProperties storageProperties;
  private final StorageService storageService;
  private final FileTypeDetector fileTypeDetector;
  private final FileAccessPolicy accessPolicy;

  @Override
  @Transactional(readOnly = true)
  public UploadedFileDto findById(Long id) {
    return fileUploadMapper.toDto(readableEntity(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<UploadedFileDto> findByApplicationId(Long applicationId) {
    ApplicationEntity application = repositoriesHandler.getApplicationEntityById(applicationId);
    if (!accessPolicy.canRead(application)) {
      throw new AccessDeniedException("Not allowed to read files of application " + applicationId);
    }
    return fileUploadMapper.toDtos(fileUploadRepository.findByApplicationId(applicationId));
  }

  @Override
  @Transactional(readOnly = true)
  public FileDownload download(Long id) {
    FileUploadEntity entity = readableEntity(id);
    return new FileDownload(
        storageService.loadAsResource(entity.getFilePath()),
        entity.getFilename(),
        entity.getContentType(),
        entity.getFileSize());
  }

  @Override
  @Transactional
  public List<UploadedFileDto> storeAll(Long applicationId, List<MultipartFile> files) {
    ApplicationEntity application = repositoriesHandler.getApplicationEntityById(applicationId);
    if (!accessPolicy.canUpload(application)) {
      throw new AccessDeniedException("Not allowed to upload to application " + applicationId);
    }

    // Validate every file before touching the disk: all-or-nothing.
    List<DetectedType> types = files.stream().map(fileTypeDetector::detect).toList();

    List<String> storedKeys = new ArrayList<>(files.size());
    // Covers failures after this method returns (flush/commit errors): files must not be orphaned.
    registerRollbackCleanup(storedKeys);

    List<FileUploadEntity> entities = new ArrayList<>(files.size());
    String directoryKey = storageProperties.attachmentsFolderName() + "/" + applicationId;

    try {
      for (int i = 0; i < files.size(); i++) {
        MultipartFile file = files.get(i);
        DetectedType type = types.get(i);
        String storedFilename = UUID.randomUUID() + type.extension();

        String key;
        try (InputStream in = file.getInputStream()) {
          key = storageService.store(directoryKey, storedFilename, in);
        } catch (IOException e) {
          throw new StorageException("Failed to read uploaded file", e);
        }
        storedKeys.add(key);

        entities.add(
            new FileUploadEntity()
                .setFilePath(key)
                .setStoredFilename(storedFilename)
                .setFilename(sanitizeDisplayName(file.getOriginalFilename(), type.extension()))
                .setFileSize(file.getSize())
                .setFileType(type.extension())
                .setContentType(type.contentType())
                .setApplication(application)
                .setCreated(LocalDate.now()));
      }
      List<FileUploadEntity> saved = new ArrayList<>(entities.size());
      fileUploadRepository.saveAll(entities).forEach(saved::add);
      return fileUploadMapper.toDtos(saved);
    } catch (RuntimeException ex) {
      log.warn("Upload failed, removing {} stored file(s): {}", storedKeys.size(), ex.getMessage());
      storedKeys.forEach(storageService::deleteQuietly);
      throw ex;
    }
  }

  /** Unauthorised callers get "not found", so file ids cannot be enumerated. */
  private FileUploadEntity readableEntity(Long id) {
    FileUploadEntity entity = repositoriesHandler.getFileUploadEntityById(id);
    if (!accessPolicy.canRead(entity.getApplication())) {
      throw new FileUploadNotFoundException("FileUploadEntity with id " + id + " not found");
    }
    return entity;
  }

  private void registerRollbackCleanup(List<String> storedKeys) {
    if (!TransactionSynchronizationManager.isSynchronizationActive()) {
      return;
    }
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCompletion(int status) {
            if (status != STATUS_COMMITTED) {
              storedKeys.forEach(storageService::deleteQuietly);
            }
          }
        });
  }

  /**
   * The client file name is only ever used as display / download metadata (never as an on-disk
   * name). Strip any directory part (both separators, since browsers/OSes differ), control
   * characters and characters that are illegal on common file systems.
   */
  static String sanitizeDisplayName(String original, String fallbackExtension) {
    String name = original == null ? "" : original;
    name = name.substring(Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\')) + 1);
    name = name.replaceAll("[\\p{Cntrl}<>:\"|?*]", "_").strip();
    if (name.isEmpty() || name.chars().allMatch(c -> c == '.')) {
      name = "file" + fallbackExtension;
    }
    if (name.length() > MAX_DISPLAY_NAME_LENGTH) {
      name = name.substring(0, MAX_DISPLAY_NAME_LENGTH);
    }
    return name;
  }
}
