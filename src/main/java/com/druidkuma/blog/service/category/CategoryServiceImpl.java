package com.druidkuma.blog.service.category;

import com.druidkuma.blog.dao.category.CategoryRepository;
import com.druidkuma.blog.dao.category.specification.CategorySpecification;
import com.druidkuma.blog.domain.BlogEntry;
import com.druidkuma.blog.domain.Category;
import com.druidkuma.blog.domain.country.Country;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
