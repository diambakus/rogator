package com.orakuma.rogator.file_upload;

import org.springframework.core.io.Resource;

/** Everything the web layer needs to serve a stored file. */
public record FileDownload(Resource resource, String filename, String contentType, Long size) {}
