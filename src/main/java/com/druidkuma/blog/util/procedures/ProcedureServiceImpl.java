package com.druidkuma.blog.util.procedures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/28/16
 */
@Service
public class ProcedureServiceImpl implements ProcedureService {

    @Autowired
    private EntityManager em;

    @Override
    public void createRandomBlogEntryCountryMappings() {
        executeWithoutParameters("create_random_blog_post_country_mappings");
    }

    @Override
    public Long resolveTranslationGroup(String groupNameKey) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("resolve_translation_group_by_full_name");
        query.registerStoredProcedureParameter("p_full_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("group_id", Integer.class, ParameterMode.OUT);
        query.setParameter("p_full_name", groupNameKey);
        query.execute();
        return ((Integer) query.getOutputParameterValue("group_id")).longValue();
    }

    private void executeWithoutParameters(String procedureName) {
        StoredProcedureQuery query = em.createStoredProcedureQuery(procedureName);
        query.execute();
    }
}
