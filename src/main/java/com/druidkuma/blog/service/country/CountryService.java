package com.druidkuma.blog.service.country;

import com.druidkuma.blog.domain.country.Country;
import com.druidkuma.blog.web.dto.CountryFlagRenderDto;

import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/22/16
 */
public interface CountryService {
    List<Country> getAvailableCountries();

    List<CountryFlagRenderDto> getCountryDataForFlags();
}
