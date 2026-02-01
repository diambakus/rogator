package com.orakuma.rogator.file_upload;

import java.util.List;
import java.util.Optional;

public class FileUploadRepositoryFk implements FileUploadRepository {
    @Override
    public <S extends FileUploadEntity> S save(S entity) {
        return null;
    }

    @Override
    public <S extends FileUploadEntity> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<FileUploadEntity> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<FileUploadEntity> findAll() {
        return null;
    }

    @Override
    public Iterable<FileUploadEntity> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(FileUploadEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends FileUploadEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<FileUploadEntity> findByApplicationId(Long id) {
        return List.of();
    }
}
