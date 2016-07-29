package com.druidkuma.blog.web;

import com.druidkuma.blog.service.category.CategoryService;
import com.druidkuma.blog.service.country.CountryService;
import com.druidkuma.blog.web.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    public CategoryResource(CategoryService categoryService, CountryService countryService) {
        this.categoryService = categoryService;
        this.countryService = countryService;
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
}
