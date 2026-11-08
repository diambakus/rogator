package com.orakuma.rogator.file_upload;

import com.orakuma.rogator.file_upload.exceptions.StorageException;
import com.orakuma.rogator.file_upload.exceptions.StorageFileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemStorageServiceTest {

  @TempDir Path tmp;
  FileSystemStorageService service;
  Path root;

  @BeforeEach
  void setUp() {
    root = tmp.resolve("not-yet-created").resolve("uploads");
    service = new FileSystemStorageService(new StorageProperties(root, null));
  }

  private static ByteArrayInputStream bytes(String s) {
    return new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  void createsRootAndRoundTrips() throws IOException {
    assertTrue(Files.isDirectory(root));
    String key = service.store("applications-attachments/42", "a.pdf", bytes("hello"));

    assertEquals("applications-attachments/42/a.pdf", key);
    Resource r = service.loadAsResource(key);
    assertEquals("hello", new String(r.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
  }

  @Test
  void leavesNoTempFilesBehind() throws IOException {
    service.store("d", "a.pdf", bytes("x"));
    try (Stream<Path> files = Files.list(root.resolve("d"))) {
      assertEquals(1, files.count());
    }
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "../evil.pdf",
        "a/../../evil.pdf",
        "/etc/passwd",
        "a//b.pdf",
        "a\\b.pdf",
        "C:\\Windows\\x.pdf",
        "c:x.pdf",
        "a/b.pdf\0",
        "a/CON*.pdf",
        ""
      })
  void rejectsUnsafeKeys(String key) {
    assertThrows(StorageException.class, () -> service.loadAsResource(key));
    assertThrows(StorageException.class, () -> service.store("x", key, bytes("x")));
  }

  @Test
  void missingFileIsNotFound() {
    assertThrows(StorageFileNotFoundException.class, () -> service.loadAsResource("d/none.pdf"));
  }

  @Test
  void symlinkEscapingRootIsRejected() throws IOException {
    Path outside = Files.writeString(tmp.resolve("secret.txt"), "secret");
    Files.createDirectories(root.resolve("d"));
    try {
      Files.createSymbolicLink(root.resolve("d/link.pdf"), outside);
    } catch (UnsupportedOperationException | IOException e) {
      return; // symlinks unavailable (e.g. Windows without privilege)
    }
    assertThrows(StorageFileNotFoundException.class, () -> service.loadAsResource("d/link.pdf"));
  }

  @Test
  void deleteQuietlyNeverThrows() {
    String key = service.store("d", "a.pdf", bytes("x"));
    service.deleteQuietly(key);
    service.deleteQuietly(key);
    service.deleteQuietly("../bad");
    assertThrows(StorageFileNotFoundException.class, () -> service.loadAsResource(key));
  }
}
