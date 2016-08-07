package com.druidkuma.blog.web.transformer;

import com.druidkuma.blog.domain.Category;
import com.druidkuma.blog.web.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

    @Autowired
    public CategoryTransformer(CountryTransformer countryTransformer) {
        this.countryTransformer = countryTransformer;
    }

    @Override
    public Category transformFromDto(CategoryDto dto) {
        throw new NotImplementedException();
    }

    @Override
    public CategoryDto tranformToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .nameKey(category.getNameKey())
                .hexColor(category.getHexColor())
                .lastModified(category.getLastModified())
                .numPosts(category.getBlogEntries().size())
                .build();
    }

    public CategoryDto tranformToBlogEditDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .nameKey(category.getNameKey())
                .hexColor(category.getHexColor())
                .countries(category.getCountries()
                        .stream()
                        .map(country -> countryTransformer.tranformToDto(country))
                        .collect(Collectors.toList()))
                .build();
    }
}
