package com.druidkuma.blog.web.transformer;

import com.druidkuma.blog.dao.category.CategoryRepository;
import com.druidkuma.blog.domain.category.Category;
import com.druidkuma.blog.web.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/5/16
 */
@Component
public class CategoryTransformer implements DtoTransformer<Category, CategoryDto> {

    private CountryTransformer countryTransformer;
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryTransformer(CountryTransformer countryTransformer, CategoryRepository categoryRepository) {
        this.countryTransformer = countryTransformer;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category transformFromDto(CategoryDto dto) {
        return categoryRepository.findOne(dto.getId());
    }

    @Override
    public CategoryDto tranformToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .nameKey(category.getNameKey())
                .hexColor(category.getHexColor())
                .textColor(category.getTextColor())
                .lastModified(category.getLastModified())
                .numPosts(category.getBlogEntries().size())
                .build();
    }

    public CategoryDto tranformToSimpleDto(Category category) {
        return CategoryDto.builder()
                .nameKey(category.getNameKey())
                .hexColor(category.getHexColor())
                .textColor(category.getTextColor())
                .build();
    }

    public CategoryDto tranformToBlogEditDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .nameKey(category.getNameKey())
                .hexColor(category.getHexColor())
                .textColor(category.getTextColor())
                .countries(category.getCountries()
                        .stream()
                        .map(country -> countryTransformer.tranformToDto(country))
                        .collect(Collectors.toList()))
                .build();
    }
}
