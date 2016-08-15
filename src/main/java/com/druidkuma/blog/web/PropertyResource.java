package com.druidkuma.blog.web;

import com.druidkuma.blog.service.property.PropertyService;
import com.druidkuma.blog.web.dto.PropertyDto;
import com.druidkuma.blog.web.transformer.PropertyTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    @Autowired
    public PropertyResource(PropertyService propertyService, PropertyTransformer propertyTransformer) {
        this.propertyService = propertyService;
        this.propertyTransformer = propertyTransformer;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PropertyDto> getPropertiesForCountry(@CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {
        return propertyService.getPropertyListForCountry(currentCountryIso).stream()
                .map(property -> propertyTransformer.tranformToDto(property))
                .collect(Collectors.toList());
    }
}
