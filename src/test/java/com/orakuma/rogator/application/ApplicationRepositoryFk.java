package com.orakuma.rogator.application;

import java.util.List;
import java.util.Optional;

public class ApplicationRepositoryFk implements ApplicationRepository {
    @Override
    public <S extends ApplicationEntity> S save(S entity) {
        return null;
    }

    @Override
    public <S extends ApplicationEntity> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<ApplicationEntity> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<ApplicationEntity> findAll() {
        return null;
    }

    @Override
    public Iterable<ApplicationEntity> findAllById(Iterable<Long> longs) {
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
    public void delete(ApplicationEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends ApplicationEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public ApplicationEntity findByName(String name) {
        return null;
    }

    @Override
    public List<ApplicationEntity> findByEmail(String email) {
        return List.of();
    }

    @Override
    public List<ApplicationEntity> findByEmailAndStatus(String email, String status) {
        return List.of();
    }
}
