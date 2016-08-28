package com.druidkuma.blog.service.i18n;

import com.druidkuma.blog.domain.country.Language;
import com.druidkuma.blog.domain.i18n.Translation;
import com.druidkuma.blog.domain.i18n.TranslationGroup;
import com.druidkuma.blog.service.excel.ExcelDocument;
import com.druidkuma.blog.web.dto.TranslationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    Page<String> getTranslationKeysForGroup(String groupNameKey, Pageable pageable, String search);

    void saveTranslation(String group, String key, String value, String countryIso);

    void saveTranslationGroup(TranslationGroup group);

    void deleteTranslationGroup(String groupName);

    void deleteTranslation(String groupName, String key);

    Map<String,Object> exportJsonTranslations(String groupName, String srcCountryIso, String destCountryIso);

    void clearForCountry(String currentCountryIso);
    void clearForAllExceptCurrent(String currentCountryIso);

    ExcelDocument exportTranslationsInExcel(String groupName, String srcCountryIso, String destCountryIso);

    byte[] exportCustomFormatTranslations(String groupName, String currentCountryIso, String targetCountry, String columnSeparator, String rowSeparator);
}
