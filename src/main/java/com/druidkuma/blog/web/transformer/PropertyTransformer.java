package com.druidkuma.blog.web.transformer;

import com.druidkuma.blog.dao.country.CountryRepository;
import com.druidkuma.blog.domain.property.Property;
import com.druidkuma.blog.util.NormalizationUtil;
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
    private CountryRepository countryRepository;

    @Autowired
    public PropertyTransformer(CountryTransformer countryTransformer, CountryRepository countryRepository) {
        this.countryTransformer = countryTransformer;
        this.countryRepository = countryRepository;
    }

    @Override
    public Property transformFromDto(PropertyDto dto) {
        return Property.builder()
                .id(dto.getId())
                .key(dto.getId() == null ? NormalizationUtil.normalizeNameKey(dto.getKey()) : dto.getKey())
                .value(dto.getValue())
                .lastModified(dto.getLastModified())
                .country(countryRepository.findByIsoAlpha2Code(dto.getCountry().getIsoCode()))
                .build();
    }

    @Override
    public PropertyDto tranformToDto(Property entity) {
        if(entity == null) return null;
        return PropertyDto.builder()
                .id(entity.getId())
                .country(countryTransformer.tranformToDto(entity.getCountry()))
                .key(entity.getKey())
                .value(entity.getValue())
                .lastModified(entity.getLastModified())
                .build();
    }
}
