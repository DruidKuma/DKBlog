package com.druidkuma.blog.util.procedures;

import org.apache.commons.lang3.tuple.Pair;

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
    Pair<Long, Long> getPreviousAndNextBlogEntryIds(Long currentBlogEntryId, String countryIso);
}
