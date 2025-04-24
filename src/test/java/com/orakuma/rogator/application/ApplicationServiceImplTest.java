package com.orakuma.rogator.application;

import java.util.List;
import java.util.Map;

public class ApplicationServiceImplTest implements ApplicationService {
    @Override
    public ApplicationDto save(ApplicationDto applicationDto) {
        return null;
    }

    @Override
    public ApplicationDto update(Long id, Map<String, Object> fieldsAndValuesMap) {
        return null;
    }

    @Override
    public ApplicationDto findById(long id) {
        return null;
    }

    @Override
    public ApplicationDto findByName(String name) {
        return null;
    }

    @Override
    public List<ApplicationDto> findAllByEmail(String email) {
        return List.of();
    }

    @Override
    public List<ApplicationDto> getAllByEmailAndStatus(String email, String status) {
        return List.of();
    }

    @Override
    public void deleteById(long id) {
    }

    @Override
    public List<ApplicationDto> getAll() {
        return List.of();
    }
}
