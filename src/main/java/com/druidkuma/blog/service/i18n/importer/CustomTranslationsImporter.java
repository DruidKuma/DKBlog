package com.druidkuma.blog.service.i18n.importer;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
    public void importTranslations(MultipartFile file, String columnSeparator, String rowSeparator) {
        //TODO
    }
}
