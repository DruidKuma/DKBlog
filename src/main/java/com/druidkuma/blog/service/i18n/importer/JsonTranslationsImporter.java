package com.druidkuma.blog.service.i18n.importer;

import com.druidkuma.blog.dao.country.LanguageRepository;
import com.druidkuma.blog.domain.i18n.TranslationGroup;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/30/16
 */
@Component
public class JsonTranslationsImporter extends TranslationsImporter {

    @Autowired
    private LanguageRepository languageRepository;

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void importTranslations(MultipartFile file, String columnSeparator, String rowSeparator) {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) responseStrBuilder.append(inputStr);
        resolveJsonTranslationsRecursively((JSONObject) new JSONParser().parse(responseStrBuilder.toString()), null, null, languageRepository.getAvailableLanguageIsoCodes());
    }

    @SuppressWarnings("unchecked")
    private void resolveJsonTranslationsRecursively(Map<String, Object> translations, TranslationGroup parentGroup, TranslationGroup currentGroup, List<String> availableLanguageIsoCodes) {
        for (Map.Entry<String, Object> entry : translations.entrySet()) {
            if(isTranslationObject(entry.getValue(), availableLanguageIsoCodes)) {
                for (Map.Entry<String, Object> newTranslations : ((Map<String, Object>) entry.getValue()).entrySet()) {
                    Map<String, Object> translationList = (Map<String, Object>) newTranslations.getValue();
                    translationList.entrySet()
                            .stream()
                            .filter(translation -> translation.getValue() instanceof String && availableLanguageIsoCodes.contains(translation.getKey()))
                            .forEach(translation ->
                                    createTranslation(
                                            newTranslations.getKey(),
                                            (String) translation.getValue(),
                                            translation.getKey(),
                                            retrieveTranslationGroup(entry.getKey(), parentGroup)));
                }
            }
            else if(entry.getValue() instanceof Map) {
                TranslationGroup newGroup = retrieveTranslationGroup(entry.getKey(), parentGroup);
                resolveJsonTranslationsRecursively((Map<String, Object>) entry.getValue(), newGroup, newGroup, availableLanguageIsoCodes);
            }
            else if (entry.getValue() instanceof String) {
                createTranslation(entry.getKey(), (String)entry.getValue(), null, currentGroup);
            }
        }
    }

    @SuppressWarnings("unchecked")
    boolean isTranslationObject(Object object, List<String> availableLanguages) {
        if(! (object instanceof Map)) return false;
        Map<String, Object> possibleTranslations = (Map<String, Object>) object;
        for (Map.Entry<String, Object> entry : possibleTranslations.entrySet()) {
            if(entry.getValue() != null && entry.getValue() instanceof Map) {
                Map<String, Object> possibleTranslationList = (Map<String, Object>) entry.getValue();
                for (Map.Entry<String, Object> translation : possibleTranslationList.entrySet()) {
                    if(translation.getValue() instanceof String && availableLanguages.contains(translation.getKey())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
