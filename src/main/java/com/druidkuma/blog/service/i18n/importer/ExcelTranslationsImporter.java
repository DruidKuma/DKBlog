package com.druidkuma.blog.service.i18n.importer;

import com.druidkuma.blog.domain.i18n.TranslationGroup;
import com.druidkuma.blog.service.excel.ExcelDocument;
import com.druidkuma.blog.service.excel.SimpleExcelDocument;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/30/16
 */
@Component
public class ExcelTranslationsImporter extends TranslationsImporter {

    @Override
    @SneakyThrows
    public void importTranslations(MultipartFile file, String columnSeparator, String rowSeparator) {
        SimpleExcelDocument document = new SimpleExcelDocument(ExcelDocument.load(file.getInputStream(), file.getOriginalFilename()));
        final List<String> headers = document.getHeaders();
        final List<String> languages = headers.subList(1, headers.size());

        for (List<String> list : document) {
            String key = list.get(0);
            List<String> langValues = list.subList(1, list.size());
            for (String translation : langValues) {
                String langIsoCode = languages.get(langValues.indexOf(translation));
                TranslationGroup translationGroup = retrieveTranslationGroup(StringUtils.substringBeforeLast(key, "."));
                createTranslation(StringUtils.substringAfterLast(key, "."), translation, langIsoCode, translationGroup);
            }
        }
    }

}
