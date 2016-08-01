package com.druidkuma.blog.web;

import com.druidkuma.blog.service.category.CategoryService;
import com.druidkuma.blog.service.country.CountryService;
import com.druidkuma.blog.web.dto.CategoryDetailedDto;
import com.druidkuma.blog.web.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CategoryDetailedDto getDetailedCategoryInfo(@PathVariable("id") Long id) {
        return categoryService.getDetailedCategoryInfo(id);
    }
}
