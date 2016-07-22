package com.druidkuma.blog.service.country;

import com.druidkuma.blog.dao.country.CountryRepository;
import com.druidkuma.blog.domain.country.Country;
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

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> getAvailableCountries() {
        return countryRepository.findAllByIsEnabledIsTrue();
    }
}
