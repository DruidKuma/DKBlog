package com.druidkuma.blog.service.country;

import com.druidkuma.blog.dao.country.CountryRepository;
import com.druidkuma.blog.dao.country.LanguageRepository;
import com.druidkuma.blog.domain.country.Country;
import com.druidkuma.blog.domain.country.Language;
import com.druidkuma.blog.web.dto.CountryFlagRenderDto;
import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/22/16
 */
@Service
public class CountryServiceImpl implements CountryService {

    private CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> getAvailableCountries() {
        return countryRepository.findAllByIsEnabledIsTrue();
    }

    @Override
    public List<CountryFlagRenderDto> getCountryDataForFlags() {
        return countryRepository.getCountryDataForFlags();
    }
}
