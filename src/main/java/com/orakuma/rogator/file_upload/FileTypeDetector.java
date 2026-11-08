package com.orakuma.rogator.file_upload;

import com.orakuma.rogator.file_upload.exceptions.InvalidFileException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Determines the real type of an upload from its leading bytes ("magic numbers"). The
 * client-supplied Content-Type and file name are never trusted.
 */
@Component
public class FileTypeDetector {

  public record DetectedType(String contentType, String extension) {}

  private record Signature(byte[] magic, DetectedType type) {}

  private static final List<Signature> SIGNATURES =
      List.of(
          new Signature(
              "%PDF-".getBytes(java.nio.charset.StandardCharsets.US_ASCII),
              new DetectedType("application/pdf", ".pdf")),
          new Signature(
              new byte[] {(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A},
              new DetectedType("image/png", ".png")),
          new Signature(
              new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF},
              new DetectedType("image/jpeg", ".jpg")));

  private static final int HEADER_LENGTH = 8;

  public DetectedType detect(MultipartFile file) {
    if (file.isEmpty()) {
      throw new InvalidFileException("Empty file is not allowed", false);
    }
    byte[] header;
    try (InputStream in = file.getInputStream()) {
      header = in.readNBytes(HEADER_LENGTH);
    } catch (IOException e) {
      throw new InvalidFileException("Unable to read uploaded file", false);
    }
    return SIGNATURES.stream()
        .filter(s -> header.length >= s.magic().length)
        .filter(s -> Arrays.equals(Arrays.copyOf(header, s.magic().length), s.magic()))
        .map(Signature::type)
        .findFirst()
        .orElseThrow(
            () -> new InvalidFileException("Unsupported file type. Allowed: PDF, PNG, JPEG", true));
  }
}
