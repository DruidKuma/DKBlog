package com.druidkuma.blog.service.i18n;

import com.druidkuma.blog.dao.country.CountryRepository;
import com.druidkuma.blog.dao.country.LanguageRepository;
import com.druidkuma.blog.dao.i18n.TranslationGroupRepository;
import com.druidkuma.blog.dao.i18n.TranslationRepository;
import com.druidkuma.blog.domain.country.Language;
import com.druidkuma.blog.domain.i18n.Translation;
import com.druidkuma.blog.domain.i18n.TranslationGroup;
import com.druidkuma.blog.exception.TranslationGroupNotExistsException;
import com.druidkuma.blog.util.procedures.ProcedureService;
import com.druidkuma.blog.web.dto.TranslationDto;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
@Service
public class TranslationServiceImpl implements TranslationService {

    private TranslationGroupRepository translationGroupRepository;
    private TranslationRepository translationRepository;
    private LanguageRepository languageRepository;
    private ProcedureService procedureService;
    private CountryRepository countryRepository;

    @Autowired
    public TranslationServiceImpl(TranslationGroupRepository translationGroupRepository, TranslationRepository translationRepository, LanguageRepository languageRepository, ProcedureService procedureService, CountryRepository countryRepository) {
        this.translationGroupRepository = translationGroupRepository;
        this.translationRepository = translationRepository;
        this.languageRepository = languageRepository;
        this.procedureService = procedureService;
        this.countryRepository = countryRepository;
    }

    @Override
    public Map<String, Object> getTranslationsForGroup(String groupNameKey, String languageIsoCode) {
        return getTranslationsForGroup(groupNameKey, languageIsoCode, true);
    }

    @Override
    public Map<String, Object> getTranslationsForGroup(String groupNameKey, String languageIsoCode, Boolean strictResolve) {
        TranslationGroup group = resolveTranslationGroup(groupNameKey);
        if(strictResolve) Assert.notNull(group);
        return transformIntoTranslations(group, languageIsoCode);
    }

    @Override
    public List<Translation> getTranslationsFromDb(TranslationGroup group, String languageIsoCode) {
        return translationRepository.findByTranslationGroupAndLanguageIsoCode(group, languageIsoCode);
    }

    @Override
    public Map<String, Translation> getTranslationsFromDb(String groupName, String languageIsoCode) {
        List<Translation> translations = translationRepository.findByTranslationGroupAndLanguageIsoCode(resolveTranslationGroup(groupName), languageIsoCode);
        Map<String, Translation> result = Maps.newHashMap();
        for (Translation translation : translations) {
            result.put(translation.getKey(), translation);
        }
        return result;
    }

    @Override
    public TranslationGroup resolveTranslationGroup(String groupNameKey) {
        String[] nestedGroups = groupNameKey.split("\\.");
        Assert.notEmpty(nestedGroups);

        TranslationGroup resultGroup = translationGroupRepository.findByNameAndParentIsNull(nestedGroups[0]);

        if(nestedGroups.length < 2) return resultGroup;
        return resolveRecursively(Arrays.copyOfRange(nestedGroups, 1, nestedGroups.length), resultGroup);
    }

    @Override
    public List<TranslationGroup> getTopLevelTranslationGroups() {
        return translationGroupRepository.findAllByParentIsNull();
    }

    @Override
    public TranslationDto getForKeyAndLanguageIso(String key, String langIso) {
        Map<String, Object> translationsForGroup = getTranslationsForGroup(StringUtils.substringBeforeLast(key, "."), langIso, false);
        String translation = (String) translationsForGroup.get(StringUtils.substringAfterLast(key, "."));
        return TranslationDto.builder()
                .display(languageRepository.findByIsoCode(langIso).getName())
                .lang(langIso)
                .value(translation).build();
    }

    @Override
    public Language getLanguageByIsoCode(String isoCode) {
        return languageRepository.findByIsoCode(isoCode);
    }

    @Override
    public List<String> getChildGroupNames(String groupNameKey) {
        List<TranslationGroup> resultGroups;
        if(StringUtils.isNotBlank(groupNameKey)) resultGroups = resolveTranslationGroup(groupNameKey).getChildGroups();
        else resultGroups = getTopLevelTranslationGroups();

        return resultGroups.stream()
                .map(TranslationGroup::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Page<String> getTranslationKeysForGroup(String groupNameKey, Pageable pageable, String search) {
        return StringUtils.isBlank(search)
                ? translationRepository.getTranslationKeysForGroup(procedureService.resolveTranslationGroup(groupNameKey), pageable)
                : translationRepository.getTranslationKeysForGroupWithSearch(procedureService.resolveTranslationGroup(groupNameKey), pageable, search);
    }

    @Override
    public void saveTranslation(String group, String key, String value, String countryIso) {
        String languageIsoCode = countryRepository.findByIsoAlpha2Code(countryIso).getDefaultLanguage().getIsoCode();
        TranslationGroup translationGroup = resolveTranslationGroup(group);
        Translation translation = translationRepository.findByTranslationGroupAndKeyAndLanguageIsoCode(translationGroup, key, languageIsoCode);
        if(translation != null) {
            translation.setValue(value);
            translation.setLastModified(Instant.now());
        }
        else translation = Translation.builder()
                .key(key)
                .value(value)
                .language(languageRepository.findByIsoCode(languageIsoCode))
                .translationGroup(translationGroup)
                .lastModified(Instant.now())
                .build();
        translationRepository.saveAndFlush(translation);
    }

    @Override
    public void saveTranslationGroup(TranslationGroup group) {
        translationGroupRepository.saveAndFlush(group);
    }

    @Override
    public void deleteTranslationGroup(String groupName) {
        TranslationGroup translationGroup = resolveTranslationGroup(groupName);
        if(translationGroup != null) {
            translationRepository.delete(translationRepository.findByTranslationGroup(translationGroup));
            translationGroupRepository.delete(translationGroup);
        }
    }

    @Override
    public void deleteTranslation(String groupName, String key) {
        TranslationGroup translationGroup = resolveTranslationGroup(groupName);
        if(translationGroup != null) {
            translationRepository.delete(translationRepository.findByTranslationGroupAndKey(translationGroup, key));
        }
    }

    @Override
    public Map<String, Object> exportJsonTranslations(String groupName, String srcCountryIso, String destCountryIso) {

        String srcLangIso = countryRepository.findByIsoAlpha2Code(srcCountryIso).getDefaultLanguage().getIsoCode();
        String destLangIso = countryRepository.findByIsoAlpha2Code(destCountryIso).getDefaultLanguage().getIsoCode();

        Map<String, Object> translations = Maps.newHashMap();
        if(StringUtils.isBlank(groupName)) {
            for (TranslationGroup translationGroup : getTopLevelTranslationGroups()) {
                translations.put(translationGroup.getName(), exportJson(translationGroup, srcLangIso, destLangIso));
            }
        }
        else {
            TranslationGroup translationGroup = resolveTranslationGroup(groupName);
            if(translationGroup == null) throw new TranslationGroupNotExistsException(groupName);
            translations.put(translationGroup.getName(), exportJson(translationGroup, srcLangIso, destLangIso));
        }
        return translations;
    }

    private Map<String, Object> exportJson(TranslationGroup group, String srcLang, String destLang) {
        Map<String, Object> groupTranslations = Maps.newHashMap();

        Map<String, Translation> srcTranslations = transformIntoTranslationMap(getTranslationsFromDb(group, srcLang));
        Map<String, Translation> destTranslations = transformIntoTranslationMap(getTranslationsFromDb(group, destLang));
        for (String key : translationRepository.getAllTranslationKeysForGroup(group)) {
            Map<String, String> translation = Maps.newHashMapWithExpectedSize(2);
            Translation srcTranslation = srcTranslations.get(key);
            Translation destTranslation = destTranslations.get(key);
            if(srcTranslation != null) translation.put(srcLang, srcTranslation.getValue());
            if(destTranslation != null) translation.put(destLang, destTranslation.getValue());

            groupTranslations.put(key, translation);
        }

        for (TranslationGroup translationGroup : group.getChildGroups()) {
            groupTranslations.put(translationGroup.getName(), exportJson(translationGroup, srcLang, destLang));
        }
        return groupTranslations;
    }

    private TranslationGroup resolveRecursively(String[] names, TranslationGroup translationGroup) {
        if(translationGroup == null || names.length < 1) return translationGroup;
        for (TranslationGroup group : translationGroup.getChildGroups()) {
            if(group.getName().equals(names[0])) return resolveRecursively(Arrays.copyOfRange(names, 1, names.length), group);
        }
        return null;
    }

    private Map<String, Object> transformIntoTranslations(TranslationGroup translationGroup, String langIsoCode) {
        Map<String, Object> result = transformTranslations(translationRepository.findByTranslationGroupAndLanguageIsoCode(translationGroup, langIsoCode));
        for (TranslationGroup group : translationGroup.getChildGroups()) {
            result.put(group.getName(), transformIntoTranslations(group, langIsoCode));
        }
        return result;
    }

    private Map<String, Object> transformTranslations(List<Translation> translations) {
        Map<String, Object> result = Maps.newHashMap();
        for (Translation translation : translations) {
            result.put(translation.getKey(), translation.getValue());
        }
        return result;
    }

    private Map<String, Translation> transformIntoTranslationMap(List<Translation> translations) {
        Map<String, Translation> result = Maps.newHashMap();
        for (Translation translation : translations) {
            result.put(translation.getKey(), translation);
        }
        return result;
    }
}
