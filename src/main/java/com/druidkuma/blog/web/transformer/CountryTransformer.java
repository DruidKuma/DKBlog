package com.druidkuma.blog.web.transformer;

import com.druidkuma.blog.dao.country.CountryRepository;
import com.druidkuma.blog.domain.country.Country;
import com.druidkuma.blog.web.dto.CountryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/5/16
 */
@Component
public class CountryTransformer implements DtoTransformer<Country, CountryDto> {

    private CountryRepository countryRepository;

    @Autowired
    public CountryTransformer(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Country transformFromDto(CountryDto dto) {
        return countryRepository.findByIsoAlpha2Code(dto.getIsoCode());
    }

    @Override
    public CountryDto tranformToDto(Country country) {
        if(country == null) return null;
        return CountryDto.builder()
                .id(country.getId())
                .defaultLanguageIso(country.getDefaultLanguage().getIsoCode())
                .isoCode(country.getIsoAlpha2Code())
                .name(country.getName())
                .build();
    }
}
