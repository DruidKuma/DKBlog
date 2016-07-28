package com.druidkuma.blog.web;

import com.druidkuma.blog.domain.Category;
import com.druidkuma.blog.service.category.CategoryService;
import com.druidkuma.blog.service.country.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Autowired
    public CategoryResource(CategoryService categoryService, CountryService countryService) {
        this.categoryService = categoryService;
        this.countryService = countryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Category> getCategoriesForCountry(@CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {
        return categoryService.getAvailableCategoriesForCountry(countryService.getCountryByIsoCode(currentCountryIso));
    }
}
