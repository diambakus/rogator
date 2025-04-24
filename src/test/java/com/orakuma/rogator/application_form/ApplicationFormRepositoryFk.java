package com.orakuma.rogator.application_form;

import java.util.List;
import java.util.Optional;

public class ApplicationFormRepositoryFk implements ApplicationFormRepository {
    @Override
    public <S extends ApplicationFormEntity> S save(S entity) {
        return null;
    }

    @Override
    public <S extends ApplicationFormEntity> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<ApplicationFormEntity> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<ApplicationFormEntity> findAll() {
        return null;
    }

    @Override
    public Iterable<ApplicationFormEntity> findAllById(Iterable<Long> longs) {
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
    public void delete(ApplicationFormEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends ApplicationFormEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public ApplicationFormEntity saveWithJsonb(ApplicationFormEntity entity, Long applicationId) {
        return null;
    }

    @Override
    public ApplicationFormEntity updateWithJsonb(ApplicationFormEntity entity, Long applicationId) {
        return null;
    }

    @Override
    public List<ApplicationFormEntity> findAllByApplicationId(Long applicationId) {
        return List.of();
    }
}
