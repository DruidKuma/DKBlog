package com.druidkuma.blog.service.country;

import com.druidkuma.blog.dao.country.CountryRepository;
import com.druidkuma.blog.dao.country.LanguageRepository;
import com.druidkuma.blog.domain.country.Country;
import com.druidkuma.blog.domain.country.Language;
import com.druidkuma.blog.util.procedures.ProcedureService;
import com.druidkuma.blog.web.dto.CountryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private ProcedureService procedureService;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, LanguageRepository languageRepository, ProcedureService procedureService) {
        this.countryRepository = countryRepository;
        this.languageRepository = languageRepository;
        this.procedureService = procedureService;
    }

    @Override
    public List<Country> getAvailableCountries() {
        return countryRepository.findAllByIsEnabledIsTrue();
    }

    @Override
    public List<CountryDto> getAll() {
        return countryRepository.getAll();
    }

    @Override
    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    @Override
    public List<CountryDto> getCountryDataForFlags() {
        return countryRepository.getCountryData();
    }

    @Override
    public Country getCountryByIsoCode(String currentCountryIso) {
        return countryRepository.findByIsoAlpha2Code(currentCountryIso);
    }

    @Override
    public void toggleCountryEnabled(String countryIso) {
        Country country = countryRepository.findByIsoAlpha2Code(countryIso);
        country.setIsEnabled(!country.getIsEnabled());
        countryRepository.saveAndFlush(country);
    }

    @Override
    public void changeDefaultLanguage(String countryIso, Language language) {
        procedureService.changeDefaultLanguageForCountry(countryIso, language.getIsoCode());
    }
}
