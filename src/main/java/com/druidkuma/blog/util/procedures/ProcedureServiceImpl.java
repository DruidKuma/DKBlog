package com.druidkuma.blog.util.procedures;

import com.druidkuma.blog.dao.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
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

    private void executeWithoutParameters(String procedureName) {
        StoredProcedureQuery query = em.createStoredProcedureQuery(procedureName);
        query.execute();
    }
}
