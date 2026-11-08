package com.orakuma.rogator.file_upload;

import com.orakuma.rogator.file_upload.exceptions.InvalidFileException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

class FileTypeDetectorTest {

  private final FileTypeDetector detector = new FileTypeDetector();

  private static MockMultipartFile file(String declaredType, byte... content) {
    return new MockMultipartFile("file", "x", declaredType, content);
  }

  @Test
  void detectsByContentNotDeclaredType() {
    var pdf = detector.detect(file("image/png", "%PDF-1.7 ...".getBytes()));
    assertEquals("application/pdf", pdf.contentType());
    assertEquals(".pdf", pdf.extension());

    var png = detector.detect(file("application/pdf", (byte) 0x89, (byte) 'P', (byte) 'N', (byte) 'G', (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A, (byte) 0));
    assertEquals(".png", png.extension());

    var jpg = detector.detect(file("text/plain", (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0));
    assertEquals("image/jpeg", jpg.contentType());
  }

  @Test
  void rejectsDisguisedAndEmptyFiles() {
    var html = assertThrows(InvalidFileException.class,
        () -> detector.detect(file("application/pdf", "<html></html>".getBytes())));
    assertTrue(html.isUnsupportedType());

    var empty = assertThrows(InvalidFileException.class, () -> detector.detect(file("application/pdf")));
    assertFalse(empty.isUnsupportedType());
  }
}
