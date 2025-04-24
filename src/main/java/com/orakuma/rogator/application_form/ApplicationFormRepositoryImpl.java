package com.orakuma.rogator.application_form;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class ApplicationFormRepositoryImpl implements ApplicationFormRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ApplicationFormEntity saveWithJsonb(ApplicationFormEntity entity, Long applicationId) {
        String sql = "INSERT INTO application_forms (position, name, application_id, content, created) " +
                "VALUES (:position, :name, :applicationId, CAST(:content AS jsonb), :created) " +
                "RETURNING id";

        Long id = (Long) entityManager
                .createNativeQuery(sql)
                .setParameter("position", entity.getPosition())
                .setParameter("name", entity.getName())
                .setParameter("applicationId", applicationId)
                .setParameter("content", entity.getContent())
                .setParameter("created", entity.getCreated())
                .getSingleResult();

        entity.setId(id);
        return entity;
    }

    @Override
    public ApplicationFormEntity updateWithJsonb(ApplicationFormEntity entity, Long formId) {
       String sql = "UPDATE application_forms SET content = CAST(:content as json), name = :name where id = :formId";

       entityManager
               .createNativeQuery(sql)
               .setParameter("name", entity.getName())
               .setParameter("content", entity.getContent())
               .setParameter("formId", formId)
               .executeUpdate();

       return entity;
    }
}
