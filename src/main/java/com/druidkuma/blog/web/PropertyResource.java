package com.druidkuma.blog.web;

import com.druidkuma.blog.domain.property.Property;
import com.druidkuma.blog.service.country.CountryService;
import com.druidkuma.blog.service.property.CommonKeys;
import com.druidkuma.blog.service.property.PropertyService;
import com.druidkuma.blog.web.dto.PropertyDto;
import com.druidkuma.blog.web.transformer.PropertyTransformer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/15/16
 */
@RestController
@RequestMapping(value = "/api/blog/property")
public class PropertyResource {

    private PropertyService propertyService;
    private PropertyTransformer propertyTransformer;
    private CountryService countryService;

    @Autowired
    public PropertyResource(PropertyService propertyService, PropertyTransformer propertyTransformer, CountryService countryService) {
        this.propertyService = propertyService;
        this.propertyTransformer = propertyTransformer;
        this.countryService = countryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PropertyDto> getPropertiesForCountry(@CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {
        return propertyService.getPropertyListForCountry(currentCountryIso).stream()
                .map(property -> propertyTransformer.tranformToDto(property))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    public void saveProperty(@RequestBody PropertyDto propertyDto) {
        propertyService.saveProperty(propertyTransformer.transformFromDto(propertyDto));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteProperty(@PathVariable("id") Long id) {
        propertyService.deleteProperty(id);
    }

    @RequestMapping(value = "/default/{key}", method = RequestMethod.GET)
    public PropertyDto getDefaultProperty(@PathVariable("key") String key) {
        return propertyTransformer.tranformToDto(propertyService.getDefaultPropertyByKey(key));
    }

    @RequestMapping(value = "/default/{id}", method = RequestMethod.PUT)
    public void makePropertyDefault(@PathVariable("id") Long id) {
        propertyService.makeDefaultPropertyById(id);
    }

    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    public void createCopyForCountry(@RequestBody PropertyDto propertyDto,
                                     @CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {
        Property property = propertyTransformer.transformFromDto(propertyDto);
        property.setId(null);
        property.setCountry(countryService.getCountryByIsoCode(currentCountryIso));
        propertyService.saveProperty(property);
    }

    @RequestMapping(value = "/specific", method = RequestMethod.GET)
    public Map<String, PropertyDto> getSpecificPropertiesForPanel(@CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {
        Map<String, PropertyDto> result = Maps.newHashMap();
        for (String key : Lists.newArrayList(CommonKeys.PERMALINK_GENERATION_STRATEGY,
                                             CommonKeys.AUTO_CREATE_DEFAULT,
                                             CommonKeys.AUTO_DELETE_DEFAULT)) {
            result.put(key, propertyTransformer.tranformToDto(propertyService.getPropertyByKeyForCountry(key, currentCountryIso)));
        }
        return result;
    }
}
