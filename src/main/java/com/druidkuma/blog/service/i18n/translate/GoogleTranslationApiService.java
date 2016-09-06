package com.druidkuma.blog.service.i18n.translate;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
@Service
public class GoogleTranslationApiService implements TranslationApiService {

    private static final String ENCODING = "UTF-8";
    private static final String BEFORE_TRANSLATION = "class=\"t0\">";
    private static final String API_URL_TEMPLATE = "https://translate.google.com/m?hl=%s&sl=%s&q=%s";

    @Override
    public String getType() {
        return "google";
    }

    @Override
    @SneakyThrows
    public String translate(String source, String srcLangIso, String destLangIso) {
        srcLangIso = URLEncoder.encode(srcLangIso, ENCODING);
        destLangIso = URLEncoder.encode(destLangIso, ENCODING);
        source = URLEncoder.encode(source, ENCODING);

        String query = String.format(API_URL_TEMPLATE, destLangIso, srcLangIso, source);
        String page = getText(query);
        return page.substring(page.indexOf(BEFORE_TRANSLATION) + BEFORE_TRANSLATION.length()).split("<")[0];
    }

    @Override
    public Map<String, String> translate(Map<String, String> srcTranslations, String srcLangIso, String destLangIso) {
        Map<String, String> destTranslations = Maps.newHashMap();
        for (Map.Entry<String, String> srcEntry : srcTranslations.entrySet()) {
            destTranslations.put(srcEntry.getKey(), translate(srcEntry.getValue(), srcLangIso, destLangIso));
        }
        return destTranslations;
    }

    private String getText(String url) throws Exception {
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("Accept-Charset", ENCODING);
        connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) response.append(inputLine);
        in.close();
        return response.toString();
    }
}
