package com.druidkuma.blog.service.i18n.importer;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/30/16
 */
@Service
public class TranslationImporterFactory {

    private static Map<String, TranslationsImporter> importerMap = Maps.newHashMap();

    @Autowired private JsonTranslationsImporter jsonTranslationsImporter;
    @Autowired private ExcelTranslationsImporter excelTranslationsImporter;
    @Autowired private CustomTranslationsImporter customTranslationsImporter;

    public static TranslationsImporter getImporter(String type) {
        TranslationsImporter importer = importerMap.get(type);
        if(importer == null) throw new RuntimeException("No Importer found for type: " + type);
        return importer;
    }

    @PostConstruct
    public void fillImporterMap() {
        importerMap.put("json", jsonTranslationsImporter);
        importerMap.put("excel", excelTranslationsImporter);
        importerMap.put("custom", customTranslationsImporter);
    }

}
