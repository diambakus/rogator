package com.orakuma.rogator.file_upload;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FileUploadServiceImplSanitizeTest {

  @Test
  void stripsDirectoriesFromAnyOs() {
    assertEquals("cv.pdf", FileUploadServiceImpl.sanitizeDisplayName("C:\\Users\\me\\cv.pdf", ".pdf"));
    assertEquals("passwd", FileUploadServiceImpl.sanitizeDisplayName("../../etc/passwd", ".pdf"));
  }

  @Test
  void replacesIllegalAndControlCharacters() {
    String n = FileUploadServiceImpl.sanitizeDisplayName("a\r\nb\"<>:|?*.pdf", ".pdf");
    assertFalse(n.matches(".*[\\p{Cntrl}\"<>:|?*].*"));
  }

  @Test
  void fallsBackWhenBlank() {
    assertEquals("file.png", FileUploadServiceImpl.sanitizeDisplayName(null, ".png"));
    assertEquals("file.png", FileUploadServiceImpl.sanitizeDisplayName("..", ".png"));
    assertEquals("file.png", FileUploadServiceImpl.sanitizeDisplayName("  ", ".png"));
  }
}
