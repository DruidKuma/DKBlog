package com.druidkuma.blog.web;

import com.druidkuma.blog.domain.i18n.Translation;
import com.druidkuma.blog.service.country.CountryService;
import com.druidkuma.blog.service.i18n.TranslationService;
import com.druidkuma.blog.web.dto.NewTranslationDto;
import com.druidkuma.blog.web.dto.SimplePaginationFilter;
import com.druidkuma.blog.web.dto.TranslatePanelDto;
import com.druidkuma.blog.web.dto.TranslationDto;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
                                                  @CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso,
                                                  SimplePaginationFilter paginationFilter) {
        return TranslatePanelDto.builder()
                .groups(translationService.getChildGroupNames(groupName))
                .translations(getPageOfTranslations(groupName, currentCountryIso, targetCountry, paginationFilter.toPageRequest("key ASC"), null))
                .build();
    }

    @RequestMapping(value = "/panel/{targetCountry}", method = RequestMethod.GET)
    public TranslatePanelDto getTopLevelTranslatePanelDto(@PathVariable("targetCountry") String targetCountry) {
        return TranslatePanelDto.builder()
                .groups(translationService.getChildGroupNames(null))
                .translations(new PageImpl<>(Lists.newArrayList(), null, 0L))
                .build();
    }

    @RequestMapping(value = "/page/{targetCountry}/{groupName:.+}", method = RequestMethod.GET)
    public Page<TranslatePanelDto.TPTranslation> getPageFilteredTranslations(@PathVariable("targetCountry") String targetCountry,
                                                                             @PathVariable("groupName") String groupName,
                                                                             @CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso,
                                                                             SimplePaginationFilter paginationFilter) {
        return getPageOfTranslations(groupName, currentCountryIso, targetCountry, paginationFilter.toPageRequest("key ASC"), paginationFilter.getSearch());
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void saveTranslation(@RequestBody NewTranslationDto newTranslationDto) {
        //TODO
        System.out.println("Implement me!");
    }

    private Page<TranslatePanelDto.TPTranslation> getPageOfTranslations(String groupName, String srcCountryIso, String destCountryIso, Pageable pageable, String search) {
        List<TranslatePanelDto.TPTranslation> translations = Lists.newArrayList();
        Map<String, Translation> sourceTranslations = translationService.getTranslationsFromDb(groupName,
                countryService.getCountryByIsoCode(srcCountryIso).getDefaultLanguage().getIsoCode());
        Map<String, Translation> targetTranslations = translationService.getTranslationsFromDb(groupName,
                countryService.getCountryByIsoCode(destCountryIso).getDefaultLanguage().getIsoCode());

        Page<String> keysPageForGroup = translationService.getTranslationKeysForGroup(groupName, pageable, search);
        for (String key : keysPageForGroup) {
            TranslatePanelDto.TPTranslation translation = TranslatePanelDto.TPTranslation.builder().key(key).build();
            if(sourceTranslations.get(key) != null) translation.setSrc(sourceTranslations.get(key).getValue());
            if(targetTranslations.get(key) != null) {
                translation.setTarget(targetTranslations.get(key).getValue());
                translation.setLastModified(targetTranslations.get(key).getLastModified());
            }
            translations.add(translation);
        }
        return new PageImpl<>(translations, pageable, keysPageForGroup.getTotalElements());
    }
}
