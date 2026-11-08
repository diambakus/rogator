package com.orakuma.rogator.file_upload;

import org.springframework.core.io.Resource;

import java.io.InputStream;

/**
 * Binary storage abstraction. Objects are addressed by a <em>storage key</em>: a relative,
 * {@code '/'}-separated path (e.g. {@code applications-attachments/42/<uuid>.pdf}) that is
 * independent of the host operating system and safe to persist in the database.
 */
public interface StorageService {

  /** Stores the stream under {@code directoryKey/filename} and returns the resulting key. */
  String store(String directoryKey, String filename, InputStream content);

  Resource loadAsResource(String key);

  /** Best-effort removal; never throws. */
  void deleteQuietly(String key);
}
