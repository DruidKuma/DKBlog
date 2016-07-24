package com.druidkuma.blog.service.i18n;

import java.util.Map;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
public interface TranslationService {

    /**
     * Retrive translations of the given group on the requested language
     * @param groupNameKey group name. Can be nested (e.g. home.sidebar.menu - means group 'menu' in group 'sidebar' in group 'home')
     * @param languageIsoCode language ISO code
     * @return
     */
    Map<String, Object> getTranslationsForGroup(String groupNameKey, String languageIsoCode);
}
