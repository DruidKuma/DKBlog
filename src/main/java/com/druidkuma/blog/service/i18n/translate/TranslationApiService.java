package com.druidkuma.blog.service.i18n.translate;

import com.druidkuma.blog.domain.country.Language;

import java.util.Map;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
public interface TranslationApiService {
    String translate(String source, String langIso);
    String translate(String source, Language language);

    Map<String, String> translate(Map<String, String> srcTranslations, String langIso);
    Map<String, String> translate(Map<String, String> srcTranslations, Language language);
}
