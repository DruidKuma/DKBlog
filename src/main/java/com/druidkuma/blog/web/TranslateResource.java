package com.druidkuma.blog.web;

import com.druidkuma.blog.service.i18n.TranslationService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/blog/i18n")
public class TranslateResource {

    private TranslationService translationService;

    @Autowired
    public TranslateResource(TranslationService translationService) {
        this.translationService = translationService;
    }

    @RequestMapping(value = "/{part}/{lang}", method = RequestMethod.GET)
    public Map<String, Object> testPartial(@PathVariable("part") String part, @PathVariable("lang") String lang) {
        if(lang.equals("de")) return Maps.newHashMap();
        return translationService.getTranslationsForGroup(part, lang);
    }
}
