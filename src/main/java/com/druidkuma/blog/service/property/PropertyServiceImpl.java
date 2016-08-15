package com.druidkuma.blog.service.property;

import com.druidkuma.blog.dao.property.PropertyRepository;
import com.druidkuma.blog.domain.property.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/15/16
 */
@Service
public class PropertyServiceImpl implements PropertyService {

    private PropertyRepository propertyRepository;

    @Autowired
    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public List<Property> getPropertyListForCountry(String countryIso) {

        //find all for country
        List<Property> propertiesForCountry = propertyRepository.findAllByCountry_IsoAlpha2Code(countryIso);

        //populate with generic properties for missing
        propertyRepository.findAllByCountryIsNull()
                .stream()
                .filter(genericProperty -> !propertiesForCountry.stream()
                        .anyMatch(property -> property.getKey().equals(genericProperty.getKey())))
                .forEach(propertiesForCountry::add);

        return propertiesForCountry;
    }
}
