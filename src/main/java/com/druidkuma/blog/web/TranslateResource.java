package com.druidkuma.blog.web;

import com.druidkuma.blog.domain.i18n.Translation;
import com.druidkuma.blog.service.country.CountryService;
import com.druidkuma.blog.service.i18n.TranslationService;
import com.druidkuma.blog.web.dto.TranslatePanelDto;
import com.druidkuma.blog.web.dto.TranslationDto;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/blog/i18n")
public class TranslateResource {

    private TranslationService translationService;
    private CountryService countryService;

    @Autowired
    public TranslateResource(TranslationService translationService, CountryService countryService) {
        this.translationService = translationService;
        this.countryService = countryService;
    }

    @RequestMapping(value = "/translate/{part}/{lang}", method = RequestMethod.GET)
    public Map<String, Object> getPartialInterfaceTranslations(@PathVariable("part") String part, @PathVariable("lang") String lang) {
        return translationService.getTranslationsForGroup(part, lang);
    }

    @RequestMapping(value = "/get/{key}/{lang}", method = RequestMethod.GET)
    public TranslationDto getTranslationForKeyAndLanguage(@PathVariable("key") String key, @PathVariable("lang") String langIso) {
        return translationService.getForKeyAndLanguageIso(key, langIso);
    }

    @RequestMapping(value = "/panel/{targetCountry}/{groupName:.+}", method = RequestMethod.GET)
    public TranslatePanelDto getTranslatePanelDto(@PathVariable("targetCountry") String targetCountry,
                                                  @PathVariable("groupName") String groupName,
                                                  @CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {
        List<String> groupNames = translationService.getChildGroupNames(groupName);
        List<TranslatePanelDto.TPTranslation> translations = Lists.newArrayList();
        Map<String, Translation> sourceTranslations = translationService.getTranslationsFromDb(groupName,
                countryService.getCountryByIsoCode(currentCountryIso).getDefaultLanguage().getIsoCode());
        Map<String, Translation> targetTranslations = translationService.getTranslationsFromDb(groupName,
                countryService.getCountryByIsoCode(targetCountry).getDefaultLanguage().getIsoCode());
        for (String key : translationService.getTranslationKeysForGroup(groupName)) {
            TranslatePanelDto.TPTranslation translation = TranslatePanelDto.TPTranslation.builder().key(key).build();
            if(sourceTranslations.get(key) != null) translation.setSrc(sourceTranslations.get(key).getValue());
            if(targetTranslations.get(key) != null) {
                translation.setTarget(targetTranslations.get(key).getValue());
                translation.setLastModified(targetTranslations.get(key).getLastModified());
            }
            translations.add(translation);
        }
        return TranslatePanelDto.builder().groups(groupNames).translations(translations).build();
    }

    @RequestMapping(value = "/panel/{targetCountry}", method = RequestMethod.GET)
    public TranslatePanelDto getTopLevelTranslatePanelDto(@PathVariable("targetCountry") String targetCountry) {
        List<String> groupNames = translationService.getChildGroupNames(null);
        return TranslatePanelDto.builder().groups(groupNames).translations(Lists.newArrayList()).build();
    }

}
