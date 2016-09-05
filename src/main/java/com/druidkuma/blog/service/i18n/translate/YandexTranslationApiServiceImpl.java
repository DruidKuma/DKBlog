package com.druidkuma.blog.service.i18n.translate;

import com.druidkuma.blog.domain.country.Language;
import com.druidkuma.blog.domain.property.Property;
import com.druidkuma.blog.service.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
@Service
public class YandexTranslationApiServiceImpl implements TranslationApiService {

    @Autowired
    private PropertyService propertyService;

    private static String API_KEY;

    @PostConstruct
    public void init() {
        API_KEY = propertyService.getString("yandex_api_key");
    }

    @Override
    public String translate(String source, String langIso) {
        return null;
    }

    @Override
    public String translate(String source, Language language) {
        return translate(source, language.getIsoCode());
    }

    @Override
    public Map<String, String> translate(Map<String, String> srcTranslations, String langIso) {
        return null;
    }

    @Override
    public Map<String, String> translate(Map<String, String> srcTranslations, Language language) {
        return translate(srcTranslations, language.getIsoCode());
    }
}
