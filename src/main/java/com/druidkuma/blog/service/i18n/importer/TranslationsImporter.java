package com.druidkuma.blog.service.i18n.importer;

import com.druidkuma.blog.dao.country.LanguageRepository;
import com.druidkuma.blog.dao.i18n.TranslationGroupRepository;
import com.druidkuma.blog.dao.i18n.TranslationRepository;
import com.druidkuma.blog.domain.i18n.Translation;
import com.druidkuma.blog.domain.i18n.TranslationGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

public abstract class TranslationsImporter {

    @Autowired private TranslationGroupRepository translationGroupRepository;
    @Autowired private TranslationRepository translationRepository;
    @Autowired private LanguageRepository languageRepository;


    public abstract void importTranslations(MultipartFile file, String columnSeparator, String rowSeparator);

    protected TranslationGroup retrieveTranslationGroup(String key, TranslationGroup parentGroup) {
        TranslationGroup existing = parentGroup == null
                ? translationGroupRepository.findByNameAndParentIsNull(key)
                : translationGroupRepository.findByNameAndParent(key, parentGroup);
        if(existing != null) return existing;

        return translationGroupRepository.saveAndFlush(
                TranslationGroup.builder()
                        .name(key)
                        .parent(parentGroup)
                        .build());
    }

    protected void createTranslation(String key, String value, String languageIsoCode, TranslationGroup group) {
        Translation translation = translationRepository.findByTranslationGroupAndKeyAndLanguageIsoCode(group, key, languageIsoCode);
        if(translation != null) {
            translation.setValue(value);
            translation.setLastModified(Instant.now());
        }
        else translation = Translation.builder()
                .key(key)
                .value(value)
                .language(languageRepository.findByIsoCode(languageIsoCode))
                .translationGroup(group)
                .lastModified(Instant.now())
                .build();
        translationRepository.saveAndFlush(translation);
    }
}