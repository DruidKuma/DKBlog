package com.druidkuma.blog.service.i18n;

import com.druidkuma.blog.dao.country.LanguageRepository;
import com.druidkuma.blog.dao.i18n.TranslationGroupRepository;
import com.druidkuma.blog.dao.i18n.TranslationRepository;
import com.druidkuma.blog.domain.country.Language;
import com.druidkuma.blog.domain.i18n.Translation;
import com.druidkuma.blog.domain.i18n.TranslationGroup;
import com.druidkuma.blog.util.procedures.ProcedureService;
import com.druidkuma.blog.web.dto.TranslationDto;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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

    @Autowired
    public TranslationServiceImpl(TranslationGroupRepository translationGroupRepository, TranslationRepository translationRepository, LanguageRepository languageRepository, ProcedureService procedureService) {
        this.translationGroupRepository = translationGroupRepository;
        this.translationRepository = translationRepository;
        this.languageRepository = languageRepository;
        this.procedureService = procedureService;
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

        TranslationGroup resultGroup = translationGroupRepository.findByName(nestedGroups[0]);

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
    public List<String> getTranslationKeysForGroup(String groupNameKey) {
        return translationRepository.getTranslationKeysForGroup(procedureService.resolveTranslationGroup(groupNameKey));
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
}
