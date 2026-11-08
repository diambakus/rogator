package com.orakuma.rogator.file_upload;

import com.orakuma.rogator.file_upload.exceptions.StorageException;
import com.orakuma.rogator.file_upload.exceptions.StorageFileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.regex.Pattern;

/**
 * Local/network file system implementation.
 *
 * <p>Portability and safety rules:
 *
 * <ul>
 *   <li>Keys are {@code '/'}-separated and restricted to {@code [A-Za-z0-9._-]} segments, so they
 *       never contain characters that are illegal or special on Windows ({@code \ : * ? " < > |}),
 *       macOS or Linux, nor reserved device names with user-controlled content.
 *   <li>Every resolved path is verified to stay inside the (real, symlink-resolved) root.
 *   <li>Writes go to a temp file in the target directory and are then moved into place, so readers
 *       never observe partial files.
 * </ul>
 */
@Service
@Slf4j
public class FileSystemStorageService implements StorageService {

  private static final Pattern SEGMENT = Pattern.compile("[A-Za-z0-9._-]{1,128}");

  private final Path root;

  public FileSystemStorageService(StorageProperties properties) {
    try {
      Path configured = properties.root().toAbsolutePath().normalize();
      Files.createDirectories(configured);
      this.root = configured.toRealPath();
      if (!Files.isWritable(root)) {
        throw new StorageException("Storage root is not writable: " + root);
      }
      log.info("File storage root: {}", root);
    } catch (IOException e) {
      throw new StorageException("Cannot initialise storage root " + properties.root(), e);
    }
  }

  @Override
  public String store(String directoryKey, String filename, InputStream content) {
    String key = directoryKey + "/" + filename;
    Path target = resolve(key);
    Path tmp = null;
    try {
      Files.createDirectories(target.getParent());
      tmp = Files.createTempFile(target.getParent(), ".upload-", ".tmp");
      Files.copy(content, tmp, StandardCopyOption.REPLACE_EXISTING);
      moveIntoPlace(tmp, target);
      return key;
    } catch (IOException e) {
      throw new StorageException("Failed to store file", e);
    } finally {
      if (tmp != null) {
        try {
          Files.deleteIfExists(tmp);
        } catch (IOException e) {
          log.warn("Could not remove temp file {}", tmp, e);
        }
      }
    }
  }

  @Override
  public Resource loadAsResource(String key) {
    Path file = resolve(key);
    try {
      // toRealPath rejects missing files and resolves symlinks that could escape the root.
      Path real = file.toRealPath();
      if (!real.startsWith(root) || !Files.isRegularFile(real) || !Files.isReadable(real)) {
        throw new StorageFileNotFoundException("Could not read file: " + key);
      }
      return new FileSystemResource(real);
    } catch (NoSuchFileException e) {
      throw new StorageFileNotFoundException("Could not read file: " + key, e);
    } catch (IOException e) {
      throw new StorageException("Failed to read file: " + key, e);
    }
  }

  @Override
  public void deleteQuietly(String key) {
    try {
      Files.deleteIfExists(resolve(key));
    } catch (IOException | RuntimeException e) {
      log.warn("Failed to delete stored file {}", key, e);
    }
  }

  private Path resolve(String key) {
    if (key == null || key.isEmpty()) {
      throw new StorageException("Empty storage key");
    }
    Path resolved = root;
    for (String segment : key.split("/", -1)) {
      if (!SEGMENT.matcher(segment).matches() || segment.equals(".") || segment.equals("..")) {
        throw new StorageException("Invalid storage key");
      }
      resolved = resolved.resolve(segment);
    }
    resolved = resolved.normalize();
    if (!resolved.startsWith(root)) {
      throw new StorageException("Invalid storage key");
    }
    return resolved;
  }

  private static void moveIntoPlace(Path source, Path target) throws IOException {
    try {
      Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
    } catch (AtomicMoveNotSupportedException e) {
      Files.move(source, target); // fails if target exists; keys are UUID based
    }
  }
}
