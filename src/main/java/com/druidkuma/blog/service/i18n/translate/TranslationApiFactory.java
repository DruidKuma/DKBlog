package com.druidkuma.blog.service.i18n.translate;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 9/6/16
 */
@Component
public class TranslationApiFactory {

    @Autowired
    private List<TranslationApiService> translationApiServices;

    private static Map<String, TranslationApiService> apiServiceCache = Maps.newHashMap();

    @PostConstruct
    public void initServicesCache() {
        for (TranslationApiService service : translationApiServices) {
            apiServiceCache.put(service.getType(), service);
        }
    }

    public static TranslationApiService getService(String type) {
        TranslationApiService service = apiServiceCache.get(type);
        if(service == null) throw new RuntimeException("Unknown translation API service type: " + type);
        return service;
    }
}
