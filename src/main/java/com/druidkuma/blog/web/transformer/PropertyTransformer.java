package com.druidkuma.blog.web.transformer;

import com.druidkuma.blog.domain.property.Property;
import com.druidkuma.blog.web.dto.PropertyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/15/16
 */
@Component
public class PropertyTransformer implements DtoTransformer<Property, PropertyDto> {

    private CountryTransformer countryTransformer;

    @Autowired
    public PropertyTransformer(CountryTransformer countryTransformer) {
        this.countryTransformer = countryTransformer;
    }

    @Override
    public Property transformFromDto(PropertyDto dto) {
        return null;
    }

    @Override
    public PropertyDto tranformToDto(Property entity) {
        return PropertyDto.builder()
                .id(entity.getId())
                .country(countryTransformer.tranformToDto(entity.getCountry()))
                .key(entity.getKey())
                .value(entity.getValue())
                .lastModified(entity.getLastModified())
                .build();
    }
}
