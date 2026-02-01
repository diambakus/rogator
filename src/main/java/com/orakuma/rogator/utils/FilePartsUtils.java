package com.orakuma.rogator.utils;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import java.util.Map;
import java.util.Set;

public final class FilePartsUtils {
    private static final Map<String, String> MIME_TO_EXTENSION = Map.of(
            "application/pdf", ".pdf",
            "image/png", ".png",
            "image/jpeg", ".jpg"
    );

    private FilePartsUtils() {}

    public static void validateFile(MultipartFile file) {
        Set<String> allowed = Set.of("application/pdf", "image/png", "image/jpeg");

        if (!allowed.contains(file.getContentType())) {
            throw new UnsupportedMediaTypeStatusException("Invalid file type");
        }
    }

    public static String resolveExtension(String contentType) {

        if (contentType == null) {
            throw new UnsupportedMediaTypeStatusException("Missing content type");
        }

        String extension = MIME_TO_EXTENSION.get(contentType.toLowerCase());

        if (extension == null) {
            throw new UnsupportedMediaTypeStatusException(
                    "Unsupported content type: " + contentType
            );
        }

        return extension;
    }

}
