package com.orakuma.rogator.file_upload;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("file")
@AllArgsConstructor
public class FileUploadController {
    private final FileUploadService fileUploadService;
}
