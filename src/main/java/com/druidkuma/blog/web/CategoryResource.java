package com.druidkuma.blog.web;

import com.druidkuma.blog.domain.Category;
import com.druidkuma.blog.domain.i18n.Translation;
import com.druidkuma.blog.domain.i18n.TranslationGroup;
import com.druidkuma.blog.service.category.CategoryService;
import com.druidkuma.blog.service.country.CountryService;
import com.druidkuma.blog.service.i18n.TranslationService;
import com.druidkuma.blog.web.dto.CategoryDetailedDto;
import com.druidkuma.blog.web.dto.CategoryDto;
import com.druidkuma.blog.web.dto.CountryFlagRenderDto;
import com.druidkuma.blog.web.dto.TranslationDto;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

import java.util.stream.Collectors;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/28/16
 */
@RestController
@RequestMapping(value = "/api/blog/category")
public class CategoryResource {

    private CategoryService categoryService;
    private CountryService countryService;
    private TranslationService translationService;

    @Autowired
    public CategoryResource(CategoryService categoryService, CountryService countryService, TranslationService translationService) {
        this.categoryService = categoryService;
        this.countryService = countryService;
        this.translationService = translationService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CategoryDto> getCategoriesForCountry(@CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {
        return categoryService.getAvailableCategoriesForCountry(countryService.getCountryByIsoCode(currentCountryIso))
                .stream()
                .map(category -> CategoryDto.builder()
                        .id(category.getId())
                        .nameKey(category.getNameKey())
                        .hexColor(category.getHexColor())
                        .lastModified(category.getLastModified())
                        .numPosts(category.getBlogEntries().size())
                        .build())
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CategoryDetailedDto getDetailedCategoryInfo(@PathVariable("id") Long id) {
        return categoryService.getDetailedCategoryInfo(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void saveCategory(@RequestBody CategoryDetailedDto categoryDto) {

        Category category = categoryDto.getId() != null ? categoryService.getById(categoryDto.getId()) : new Category();

        if(category.getNameKey() == null) category.setNameKey(categoryDto.getNameKey());

        if(category.getCountries() == null) category.setCountries(Sets.newHashSet());
        category.getCountries().clear();
        for (CountryFlagRenderDto flagRenderDto : categoryDto.getCountries()) {
            category.getCountries().add(countryService.getCountryByIsoCode(flagRenderDto.getIsoCode()));
        }
        category.setHexColor(categoryDto.getHexColor());
        category.setLastModified(Instant.now());

        TranslationGroup translationGroup = translationService.resolveTranslationGroup("components.category");
        if(category.getTranslations() == null) category.setTranslations(Lists.newArrayList());
        for (TranslationDto translationDto : categoryDto.getTranslations()) {
            boolean updated = false;
            for (Translation translation : category.getTranslations()) {
                if(translation.getLanguage().getIsoCode().equals(translationDto.getLang())) {
                    translation.setValue(translationDto.getValue());
                    translation.setLastModified(Instant.now());
                    updated = true;
                    break;
                }
            }
            if(!updated) {
                category.getTranslations().add(Translation.builder()
                        .key(categoryDto.getNameKey())
                        .language(translationService.getLanguageByIsoCode(translationDto.getLang()))
                        .lastModified(Instant.now())
                        .translationGroup(translationGroup)
                        .value(translationDto.getValue())
                        .build());
            }
        }
        categoryService.saveCategory(category);
    }
}
