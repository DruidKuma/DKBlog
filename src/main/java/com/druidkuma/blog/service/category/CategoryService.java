package com.druidkuma.blog.service.category;

import com.druidkuma.blog.domain.entry.BlogEntry;
import com.druidkuma.blog.domain.category.Category;
import com.druidkuma.blog.domain.country.Country;
import com.druidkuma.blog.web.dto.category.CategoryDetailedDto;

import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/28/16
 */
public interface CategoryService {
    List<Category> getAvailableCategoriesForEntry(BlogEntry entry);
    List<Category> getAvailableCategoriesForCountry(Country country);

    List<Category> getAvailableCategoriesForCountryInOrder(String countryIso);
    void updateCategorySortOrderForCountry(List<Long> categorIds, String countryIso);

    List<Category> getAvailableCategoriesForCountries(List<Country> countries);
    boolean isCategoryAvailableInCountries(Category category, List<Country> countries);
    boolean isCategoryAvailableInCountry(Category category, Country country);
    CategoryDetailedDto getDetailedCategoryInfo(Long id);

    Category getById(Long id);
    void saveCategory(Category category);
    void removeCountryFromCategory(Long id, String countryIso);

    void removeCategory(Long id);
}
