package com.druidkuma.blog.util.procedures;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/28/16
 */
public interface ProcedureService {
    void createRandomBlogEntryCountryMappings();
    Long resolveTranslationGroup(String groupNameKey);
}
