package com.druidkuma.blog.web.transformer;

import com.druidkuma.blog.domain.country.Country;
import com.druidkuma.blog.web.dto.CountryFlagRenderDto;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/5/16
 */
@Component
public class CountryTransformer implements DtoTransformer<Country, CountryFlagRenderDto> {
    @Override
    public Country transformFromDto(CountryFlagRenderDto dto) {
        throw new NotImplementedException();
    }

    @Override
    public CountryFlagRenderDto tranformToDto(Country country) {
        return CountryFlagRenderDto.builder()
                .id(country.getId())
                .defaultLanguageIso(country.getDefaultLanguage().getIsoCode())
                .isoCode(country.getIsoAlpha2Code())
                .name(country.getName())
                .build();
    }
}
