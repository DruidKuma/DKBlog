package com.druidkuma.blog.web;

import com.druidkuma.blog.domain.property.Property;
import com.druidkuma.blog.service.country.CountryService;
import com.druidkuma.blog.service.property.PropertyService;
import com.druidkuma.blog.web.dto.PropertyDto;
import com.druidkuma.blog.web.dto.filter.SimplePaginationFilter;
import com.druidkuma.blog.web.transformer.PropertyTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<PropertyDto> getPageOfPropertiesForCountry(SimplePaginationFilter paginationFilter,
                                                           @CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {
        Pageable pageable = paginationFilter.toPageRequest("lastModified DESC");
        Page<Property> properties = propertyService.getPropertyPageForCountry(pageable, paginationFilter.getSearch(), currentCountryIso);

        return new PageImpl<>(
                properties.getContent()
                        .stream()
                        .map(property -> propertyTransformer.tranformToDto(property))
                        .collect(Collectors.toList()),
                pageable, properties.getTotalElements());
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
        return propertyTransformer.tranformToDto(propertyService.getProperty(key));
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

    @RequestMapping(value = "/file/all", method = RequestMethod.GET)
    public Map<String, Object> getAllFileProperties() {
        return propertyService.getAllFileProperties();
    }

    @RequestMapping(value = "/system/all", method = RequestMethod.GET)
    public Map<String, String> getAllSystemProperties() {
        return propertyService.getAllSystemProperties();
    }
}
