package com.druidkuma.blog.service.category;

import com.druidkuma.blog.dao.category.CategoryRepository;
import com.druidkuma.blog.dao.category.specification.CategorySpecification;
import com.druidkuma.blog.domain.BlogEntry;
import com.druidkuma.blog.domain.Category;
import com.druidkuma.blog.domain.country.Country;
import com.druidkuma.blog.domain.i18n.Translation;
import com.druidkuma.blog.web.dto.CategoryDetailedDto;
import com.druidkuma.blog.web.dto.CountryFlagRenderDto;
import com.druidkuma.blog.web.dto.TranslationDto;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/28/16
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAvailableCategoriesForEntry(BlogEntry entry) {
        return getAvailableCategoriesForCountries(entry.getCountries());
    }

    @Override
    public List<Category> getAvailableCategoriesForCountry(Country country) {
        return getAvailableCategoriesForCountries(Lists.newArrayList(country));
    }

    @Override
    public List<Category> getAvailableCategoriesForCountries(List<Country> countries) {
        return categoryRepository.findAll(new CategorySpecification(countries));
    }

    @Override
    public boolean isCategoryAvailableInCountries(Category category, List<Country> countries) {
        return category.getCountries().containsAll(countries);
    }

    @Override
    public boolean isCategoryAvailableInCountry(Category category, Country country) {
        return isCategoryAvailableInCountries(category, Lists.newArrayList(country));
    }

    @Override
    public CategoryDetailedDto getDetailedCategoryInfo(Long id) {
        Category category = categoryRepository.findOne(id);
        return CategoryDetailedDto.builder()
                .hexColor(category.getHexColor())
                .nameKey(category.getNameKey())
                .id(category.getId())
                .countries(getCountryFlagData(category.getCountries()))
                .translations(getTranslationData(category.getTranslations()))
                .build();
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findOne(id);
    }

    @Override
    public void saveCategory(Category category) {
        categoryRepository.saveAndFlush(category);
    }

    @Override
    public void removeCountryFromCategory(Long id, String countryIso) {
        Category category = categoryRepository.findOne(id);
        Iterator<Country> countryIterator = category.getCountries().iterator();
        while(countryIterator.hasNext()) {
            Country country = countryIterator.next();
            if(country.getIsoAlpha2Code().equals(countryIso)) {
                countryIterator.remove();
                break;
            }
        }
        categoryRepository.saveAndFlush(category);
    }

    @Override
    public void removeCategory(Long id) {
        categoryRepository.delete(id);
    }

    private List<TranslationDto> getTranslationData(List<Translation> translations) {
        return translations.stream().map(translation -> TranslationDto.builder()
                .value(translation.getValue())
                .lang(translation.getLanguage().getIsoCode())
                .display(translation.getLanguage().getName())
                .build()).collect(Collectors.toList());
    }

    private List<CountryFlagRenderDto> getCountryFlagData(Set<Country> countries) {
        return countries.stream().map(country -> CountryFlagRenderDto.builder()
                .id(country.getId())
                .defaultLanguageIso(country.getDefaultLanguage().getIsoCode())
                .isoCode(country.getIsoAlpha2Code())
                .name(country.getName())
                .build()).collect(Collectors.toList());
    }
}