package com.orakuma.rogator.file_upload;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileUploadRepository extends CrudRepository<FileUploadEntity, Long> {
  List<FileUploadEntity> findByApplicationId(Long id);
}
