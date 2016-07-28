package com.druidkuma.blog.util;

import com.druidkuma.blog.dao.country.LanguageRepository;
import com.druidkuma.blog.dao.i18n.TranslationGroupRepository;
import com.druidkuma.blog.dao.i18n.TranslationRepository;
import com.druidkuma.blog.domain.i18n.Translation;
import com.druidkuma.blog.domain.i18n.TranslationGroup;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/15/16
 */
@Service
public class WordpressBlogMigrator {

    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private TranslationRepository translationRepository;
    @Autowired
    private TranslationGroupRepository translationGroupRepository;

    public static boolean exists(String URLName) throws IOException {
        try {
            final URL url = new URL(URLName);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            return huc.getResponseCode() == 200;
        } catch (Exception e) {
            System.out.println("Declined blog entry, image does not exist");
            return false;
        }
    }

//    @PostConstruct
    public void createTranslationFromJson() throws IOException, ParseException {
        for (String lang : Arrays.asList("en", "ru")) {
            JSONObject translations = (JSONObject) new JSONParser().parse(new FileReader(this.getClass().getClassLoader().getResource(String.format("db/translations_%s.json", lang)).getFile()));
            resolveRecursively(translations, null, null, lang);
        }
    }

    private void resolveRecursively(Map<String, Object> translations, TranslationGroup parentGroup, TranslationGroup currentGroup, String languageIsoCode) {
        for (Map.Entry<String, Object> entry : translations.entrySet()) {
            if(entry.getValue() instanceof Map) {
                TranslationGroup newGroup = createTranslationGroup(entry.getKey(), parentGroup);
                resolveRecursively((Map<String, Object>) entry.getValue(), newGroup, newGroup, languageIsoCode);
            }
            else if (entry.getValue() instanceof String) {
                createTranslation(entry.getKey(), (String)entry.getValue(), languageIsoCode, currentGroup);
            }
        }
    }

    private void createTranslation(String key, String value, String languageIsoCode, TranslationGroup group) {
        Translation translation = translationRepository.findByTranslationGroupAndKeyAndLanguageIsoCode(group, key, languageIsoCode);
        if(translation != null) translation.setValue(value);
        else translation = Translation.builder()
                .key(key)
                .value(value)
                .language(languageRepository.findByIsoCode(languageIsoCode))
                .translationGroup(group)
                .lastModified(Instant.now())
                .build();
        translationRepository.saveAndFlush(translation);
    }

    private TranslationGroup createTranslationGroup(String key, TranslationGroup parentGroup) {
        TranslationGroup existing = translationGroupRepository.findByName(key);
        if(existing != null) return existing;

        return translationGroupRepository.saveAndFlush(
                TranslationGroup.builder()
                        .name(key)
                        .parent(parentGroup)
                        .build());
    }
}
