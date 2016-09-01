package com.druidkuma.blog.service.i18n.importer;

import com.druidkuma.blog.domain.i18n.TranslationGroup;
import com.druidkuma.blog.util.Strings;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/30/16
 */
@Component
public class CustomTranslationsImporter extends TranslationsImporter {

    @Override
    @SneakyThrows
    public void importTranslations(MultipartFile file, String columnSeparator, String rowSeparator) {
        if(Strings.isEmpty(columnSeparator)) columnSeparator = ",";

        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));

        List<String> header = readRow(reader, rowSeparator, columnSeparator);
        List<String> languages = header.subList(1, header.size());

        List<String> translationRow;
        while (!(translationRow = readRow(reader, rowSeparator, columnSeparator)).isEmpty()) {
            String key = translationRow.get(0);
            List<String> langValues = translationRow.subList(1, translationRow.size());
            for (String translation : langValues) {
                String langIsoCode = languages.get(langValues.indexOf(translation));
                TranslationGroup translationGroup = retrieveTranslationGroup(StringUtils.substringBeforeLast(key, "."));
                createTranslation(StringUtils.substringAfterLast(key, "."), translation, langIsoCode, translationGroup);
            }
        }
    }

    private List<String> readRow(BufferedReader reader, String rowSeparator, String columnSeparator) throws IOException {
        String line = reader.readLine();
        return line == null ? Collections.emptyList() : Arrays.asList(Strings.substringBeforeLast(line, rowSeparator).split(columnSeparator));
    }
}
