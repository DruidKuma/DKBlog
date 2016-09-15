package com.druidkuma.blog.web;

import com.druidkuma.blog.domain.country.Country;
import com.druidkuma.blog.domain.i18n.Translation;
import com.druidkuma.blog.domain.i18n.TranslationGroup;
import com.druidkuma.blog.exception.TranslationGroupExistsException;
import com.druidkuma.blog.exception.TranslationGroupNotExistsException;
import com.druidkuma.blog.service.country.CountryService;
import com.druidkuma.blog.service.excel.ExcelDocument;
import com.druidkuma.blog.service.i18n.TranslationService;
import com.druidkuma.blog.web.dto.*;
import com.druidkuma.blog.web.dto.country.CountryConfigDto;
import com.druidkuma.blog.web.dto.country.CountryDto;
import com.druidkuma.blog.web.dto.country.LanguageDto;
import com.druidkuma.blog.web.dto.filter.SimplePaginationFilter;
import com.druidkuma.blog.web.dto.i18n.ExternalTranslateConfigDto;
import com.druidkuma.blog.web.dto.i18n.NewTranslationDto;
import com.druidkuma.blog.web.dto.i18n.TranslatePanelDto;
import com.druidkuma.blog.web.dto.i18n.TranslationDto;
import com.druidkuma.blog.web.transformer.LanguageTransformer;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/blog/i18n")
public class TranslateResource {

    private TranslationService translationService;
    private CountryService countryService;
    private LanguageTransformer languageTransformer;

    @Autowired
    public TranslateResource(TranslationService translationService, CountryService countryService, LanguageTransformer languageTransformer) {
        this.translationService = translationService;
        this.countryService = countryService;
        this.languageTransformer = languageTransformer;
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
        translationService.saveTranslation(newTranslationDto.getGroup(),
                newTranslationDto.getKey(),
                newTranslationDto.getValue(),
                newTranslationDto.getCountryIso());
    }

    @RequestMapping(value = "/group/save", method = RequestMethod.POST)
    public void saveTranslationGroup(@RequestBody NewTranslationDto translationGroupDto) {
        TranslationGroup translationGroup = null;
        if(StringUtils.isNotBlank(translationGroupDto.getGroup())) {
            translationGroup = translationService.resolveTranslationGroup(translationGroupDto.getGroup());
            if(translationGroup == null) throw new TranslationGroupNotExistsException(translationGroupDto.getGroup());
            for (TranslationGroup group : translationGroup.getChildGroups()) {
                if(group.getName().equals(translationGroupDto.getKey())) {
                    throw new TranslationGroupExistsException(translationGroupDto.getGroup(), translationGroupDto.getKey());
                }
            }
        }
        translationService.saveTranslationGroup(
                TranslationGroup.builder()
                        .name(translationGroupDto.getKey())
                        .parent(translationGroup)
                        .build());
    }

    @RequestMapping(value = "/group/remove/{groupName:.+}", method = RequestMethod.DELETE)
    public void deleteTranslationGroup(@PathVariable("groupName") String groupName) {
        translationService.deleteTranslationGroup(groupName);
    }

    @RequestMapping(value = "/translation/remove/{groupName:.+}/{key}", method = RequestMethod.DELETE)
    public void deleteTranslation(@PathVariable("groupName") String groupName, @PathVariable("key") String key) {
        translationService.deleteTranslation(groupName, key);
    }

    @RequestMapping(value = "/translation/remove/country/{countryIso}", method = RequestMethod.DELETE)
    public void clearTranslationsForCountry(@PathVariable("countryIso") String countryIso) {
        translationService.clearForCountry(countryIso);
    }

    @RequestMapping(value = "/translation/remove/country/all", method = RequestMethod.DELETE)
    public void clearTranslationsForAllCountries(@CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {
        translationService.clearForAllExceptCurrent(currentCountryIso);
    }

    @RequestMapping(value = "/export/json/{targetCountry}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<Map<String, Object>> exportTranslationsInJson(@PathVariable("targetCountry") String targetCountry,
                                                        @CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {
        return exportTranslationsInJson(null, currentCountryIso, targetCountry);
    }

    @RequestMapping(value = "/export/json/{targetCountry}/{groupName:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<Map<String, Object>> exportTranslationsInJson(@PathVariable("groupName") String groupName,
                                               @PathVariable("targetCountry") String targetCountry,
                                               @CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {

        return buildEntityForDownloadFile(translationService.exportJsonTranslations(groupName, currentCountryIso, targetCountry));
    }

    @RequestMapping(value = "/export/xls/{targetCountry}")
    public HttpEntity<byte[]> downloadInExcel(@PathVariable("targetCountry") String targetCountry,
                                              @CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {
        return downloadInExcel(null, targetCountry, currentCountryIso);
    }

    @RequestMapping(value = "/export/xls/{targetCountry}/{groupName:.+}")
    public HttpEntity<byte[]> downloadInExcel(@PathVariable("groupName") String groupName,
                                              @PathVariable("targetCountry") String targetCountry,
                                              @CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {

        ExcelDocument excelDocument = translationService.exportTranslationsInExcel(groupName, currentCountryIso, targetCountry);
        return buildHttpEntityWithExcelBytesAndHeaders(excelDocument.getBytes());
    }

    @RequestMapping(value = "/export/custom", method = RequestMethod.POST)
    public HttpEntity<byte[]> downloadInCustomFormat(@RequestBody ExportConfigDto exportConfigDto,
                                              @CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {
        return buildHttpEntityWithTextBytesAndHeaders(
                translationService.exportCustomFormatTranslations(
                        exportConfigDto.getGroupName(),
                        currentCountryIso,
                        exportConfigDto.getTargetCountry(),
                        exportConfigDto.getColumnSeparator(),
                        exportConfigDto.getRowSeparator()
                ));
    }

    @RequestMapping(value = "/import/{type}", method = RequestMethod.POST)
    @SneakyThrows
    public void uploadTranslations(@RequestParam(value = "file") MultipartFile file,
                                   @RequestParam(value = "columnSeparator", required = false) String columnSeparator,
                                   @RequestParam(value = "rowSeparator", required = false) String rowSeparator,
                                   @PathVariable("type") String type) {
        translationService.importTranslations(file, type, columnSeparator, rowSeparator);
    }

    @RequestMapping(value = "/translate/external", method = RequestMethod.POST)
    public void translateWithExternalApi(@RequestBody ExternalTranslateConfigDto translateConfigDto) {
        translationService.translateWithExternalService(
                translateConfigDto.getGroup(),
                translateConfigDto.getSrcCountry().getDefaultLanguageIso(),
                translateConfigDto.getDestCountry().getDefaultLanguageIso(),
                translateConfigDto.getType(),
                translateConfigDto.getOverride());
    }

    @RequestMapping(value = "/translate/full", method = RequestMethod.POST)
    public void launchFullTranslate(@CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {
        translationService.performFullTranslation(currentCountryIso);
    }

    @RequestMapping(value = "/data/country/{countryIso}", method = RequestMethod.GET)
    public CountryConfigDto getConfigCountryData(@PathVariable("countryIso") String countryIso) {
        Country country = countryService.getCountryByIsoCode(countryIso);
        return CountryConfigDto
                .builder()
                .enabled(country.getIsEnabled())
                .name(country.getName())
                .isoCode(country.getIsoAlpha2Code())
                .defaultLanguage(languageTransformer.tranformToDto(country.getDefaultLanguage()))
                .languages(country.getLanguages()
                        .stream()
                        .map(language -> languageTransformer.tranformToDto(language))
                        .collect(Collectors.toList()))
                .build();
    }

    @RequestMapping(value = "/data/names/country", method = RequestMethod.GET)
    public List<CountryDto> getConfigCountryData() {
        return countryService.getAll();
    }

    @RequestMapping(value = "/data/country/enabled/{isoCode}", method = RequestMethod.POST)
    public void toggleCountryEnabled(@PathVariable("isoCode") String isoCode) {
        countryService.toggleCountryEnabled(isoCode);
    }

    @RequestMapping(value = "/data/country/language/default/{countryIso}")
    public void changeDefaultLanguageForCountry(@PathVariable("countryIso") String countryIso, @RequestBody LanguageDto languageDto) {
        countryService.changeDefaultLanguage(countryIso, languageTransformer.transformFromDto(languageDto));
    }

    @RequestMapping(value = "/data/languages", method = RequestMethod.GET)
    public List<LanguageDto> getAllLanguages() {
        return countryService.getAllLanguages()
                .stream()
                .map(language -> languageTransformer.tranformToDto(language))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/data/languages/save/{countryIso}", method = RequestMethod.POST)
    public void saveCountryLanguages(@RequestBody List<LanguageDto> languages, @PathVariable("countryIso") String countryIso) {
        countryService.updateLanguagesForCountry(
                countryIso,
                languages
                        .stream()
                        .map(languageDto -> languageTransformer.transformFromDto(languageDto))
                        .collect(Collectors.toList())
        );
    }

    private HttpEntity<byte[]> buildHttpEntityWithTextBytesAndHeaders(byte[] bytes) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain; charset=utf-8");
        headers.add("Content-Disposition", "attachment; filename=translations.txt");
        return new ResponseEntity<>(bytes, headers, OK);
    }

    private HttpEntity<byte[]> buildHttpEntityWithExcelBytesAndHeaders(byte[] bytes) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        headers.add("Content-Disposition", "attachment; filename=translations.xlsx");
        return new ResponseEntity<>(bytes, headers, OK);
    }

    private HttpEntity<Map<String, Object>> buildEntityForDownloadFile(Map<String, Object> bytes) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Content-Disposition", "attachment; filename=translations.json");
        return new ResponseEntity<>(bytes, headers, OK);
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
