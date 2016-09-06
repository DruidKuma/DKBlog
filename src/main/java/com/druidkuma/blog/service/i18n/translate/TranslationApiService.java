package com.druidkuma.blog.service.i18n.translate;

import java.util.Map;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
public interface TranslationApiService {
    String getType();
    String translate(String source, String srcLangIso, String destLangIso);
    Map<String, String> translate(Map<String, String> srcTranslations, String srcLangIso, String destLangIso);
}
