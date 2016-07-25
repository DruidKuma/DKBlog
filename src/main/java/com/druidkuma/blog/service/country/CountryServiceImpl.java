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
    private LanguageRepository languageRepository;

    private LoadingCache<Long, Language> countryIsoToDefaultLanguageCache;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, LanguageRepository languageRepository) {
        this.countryRepository = countryRepository;
        this.languageRepository = languageRepository;
    }

    @Override
    public List<Country> getAvailableCountries() {
        return countryRepository.findAllByIsEnabledIsTrue();
    }

    @Override
    public List<CountryFlagRenderDto> getCountryDataForFlags() {
        //TODO refactor this shit after https://hibernate.atlassian.net/browse/HHH-4335 will be fixed finally
        List<CountryFlagRenderDto> dataForFlags = countryRepository.getCountryDataForFlags();
        for (CountryFlagRenderDto dataForFlag : dataForFlags) {
            Language language = countryIsoToDefaultLanguageCache.getUnchecked(dataForFlag.getId());
            if(language != null) dataForFlag.setDefaultLanguageIso(language.getIsoCode());
        }
        return dataForFlags;
    }

    @PostConstruct
    public void configureCache() {
        countryIsoToDefaultLanguageCache = CacheBuilder.newBuilder()
                .concurrencyLevel(1)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .expireAfterAccess(1, TimeUnit.HOURS)
                .maximumSize(300L)
                .build(CacheLoader.from(new Function<Long, Language>() {
                    @Override
                    public Language apply(Long countryId) {
                        Long languageId = languageRepository.getDefaultLanguageForCountry(countryId);
                        if(languageId != null) return languageRepository.findOne(languageId);
                        return languageRepository.findByIsoCode("en");
                    }
                }));
    }
}
