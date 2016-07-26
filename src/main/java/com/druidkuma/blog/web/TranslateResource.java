package com.druidkuma.blog.web;

import com.druidkuma.blog.domain.i18n.TranslationGroup;
import com.druidkuma.blog.service.i18n.TranslationService;
import com.druidkuma.blog.web.dto.TranslatePanelDto;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @RequestMapping(value = "/translate/{part}/{lang}", method = RequestMethod.GET)
    public Map<String, Object> getPartialInterfaceTranslations(@PathVariable("part") String part, @PathVariable("lang") String lang) {
        return translationService.getTranslationsForGroup(part, lang);
    }

    @RequestMapping(value = "/panel/{langFrom}/{langTo}/{groupKey}")
    public Map<String, Object> getDataForTranslationPanel(@PathVariable("langFrom") String langFrom,
                                                          @PathVariable("langTo") String langTo,
                                                          @PathVariable("groupKey") String groupKey) {
        Map<String, Object> menu = Maps.newHashMap();
        menu.put("allPosts", "AllPosts");
        menu.put("i18nPanel", "I18N Panel");
        menu.put("dashboard", "Dashboard");

        Map<String, Object> sidebar = Maps.newHashMap();
        sidebar.put("menu", menu);
        sidebar.put("switcher", "Country Switcher");
        sidebar.put("Siska", "Sosiska");

        Map<String, Object> home = Maps.newHashMap();
        home.put("sidebar", sidebar);

        return home;
    }

    @RequestMapping(value = "/panel/data/{langFrom}/{langTo}/{groupKey}")
    public TranslatePanelDto getData(@PathVariable("langFrom") String langFrom, @PathVariable("langTo") String langTo, @PathVariable("groupKey") String groupKey) {
        TranslationGroup translationGroup = translationService.resolveTranslationGroup(groupKey);
        return TranslatePanelDto
                .builder()
                .displayName(groupKey)
                .translations(translationService.getTranslationsFromDb(translationGroup, "en"))
                .groupId(translationGroup.getId())
                .childGroupNames(translationGroup.getChildGroups().stream().map(TranslationGroup::getName).collect(Collectors.toList()))
                .build();
    }

    @RequestMapping(value = "/panel/names/{parentGroupKey}", method = RequestMethod.GET)
    public List<String> getGroupNames(@PathVariable("parentGroupKey") String parentGroupKey) {
        return translationService.resolveTranslationGroup(parentGroupKey).getChildGroups().stream()
                .map(TranslationGroup::getName)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/panel/names", method = RequestMethod.GET)
    public List<String> getGroupNames() {
        return translationService.getTopLevelTranslationGroups().stream()
                .map(TranslationGroup::getName)
                .collect(Collectors.toList());
    }


}
