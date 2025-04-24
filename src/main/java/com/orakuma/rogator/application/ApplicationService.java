package com.orakuma.rogator.application;

import java.util.List;
import java.util.Map;

public interface ApplicationService {
    ApplicationDto save(ApplicationDto applicationDto);
    ApplicationDto update(Long id, Map<String, Object> fieldsAndValuesMap);
    ApplicationDto findById(long id);
    ApplicationDto findByName(String name);
    List<ApplicationDto> findAllByEmail(String email);
    List<ApplicationDto> getAllByEmailAndStatus(String email, String status);
    void deleteById(long id);
    List<ApplicationDto> getAll();
}
