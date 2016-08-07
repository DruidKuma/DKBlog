package com.druidkuma.blog.web;

import com.druidkuma.blog.service.country.CountryService;
import com.druidkuma.blog.web.dto.CountryFlagRenderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/22/16
 */
@RestController
@RequestMapping(value = "/api/blog/country")
public class CountryResource {

    private CountryService countryService;

    @Autowired
    public CountryResource(CountryService countryService) {
        this.countryService = countryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CountryFlagRenderDto> getCountryDataForFlags() {
        return countryService.getCountryDataForFlags();
    }
}
