package com.druidkuma.blog.util.procedures;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.Tuple;

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

    @Override
    public Pair<Long, Long> getPreviousAndNextBlogEntryIds(Long currentBlogEntryId) {
        return Pair.of(
                getShiftedBlogEntryId("select_previous_blog_entry_id", currentBlogEntryId),
                getShiftedBlogEntryId("select_next_blog_entry_id", currentBlogEntryId));
    }

    private Long getShiftedBlogEntryId(String procedureName, Long currentBlogEntryId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery(procedureName);
        query.registerStoredProcedureParameter("p_blog_entry_id", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("shifted_id", Integer.class, ParameterMode.OUT);
        query.setParameter("p_blog_entry_id", currentBlogEntryId);
        query.execute();
        return ((Integer) query.getOutputParameterValue("shifted_id")).longValue();
    }

    private void executeWithoutParameters(String procedureName) {
        StoredProcedureQuery query = em.createStoredProcedureQuery(procedureName);
        query.execute();
    }
}
