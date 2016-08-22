package com.druidkuma.blog.service.i18n;

import com.druidkuma.blog.domain.country.Language;
import com.druidkuma.blog.domain.i18n.Translation;
import com.druidkuma.blog.domain.i18n.TranslationGroup;
import com.druidkuma.blog.web.dto.TranslationDto;

import java.util.List;
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
    Map<String, Object> getTranslationsForGroup(String groupNameKey, String languageIsoCode, Boolean strictResolve);
    List<Translation> getTranslationsFromDb(TranslationGroup translationGroup, String languageIsoCode);

    Map<String, Translation> getTranslationsFromDb(String groupName, String languageIsoCode);

    TranslationGroup resolveTranslationGroup(String groupNameKey);
    List<TranslationGroup> getTopLevelTranslationGroups();
    TranslationDto getForKeyAndLanguageIso(String key, String langIso);
    Language getLanguageByIsoCode(String isoCode);

    List<String> getChildGroupNames(String groupNameKey);
    List<String> getTranslationKeysForGroup(String groupNameKey);
}
