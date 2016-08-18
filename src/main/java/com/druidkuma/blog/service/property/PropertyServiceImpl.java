package com.druidkuma.blog.service.property;

import com.druidkuma.blog.dao.property.PropertyRepository;
import com.druidkuma.blog.domain.property.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
        return propertyRepository.findAllForCountryOrDefault(countryIso);
    }

    @Override
    public void saveProperty(Property property) {
        propertyRepository.saveAndFlush(property);
    }

    @Override
    public void deleteProperty(Long id) {
        propertyRepository.delete(id);
    }

    @Override
    public Property getDefaultPropertyByKey(String key) {
        return propertyRepository.findByKeyAndCountryIsNull(key);
    }

    @Override
    public void makeDefaultPropertyById(Long id) {
        Property property = propertyRepository.findOne(id);
        Property currentDefault = propertyRepository.findByKeyAndCountryIsNull(property.getKey());

        if(currentDefault == null) {
            currentDefault = new Property();
            currentDefault.setKey(property.getKey());
        }

        currentDefault.setValue(property.getValue());
        currentDefault.setLastModified(Instant.now());
        propertyRepository.saveAndFlush(currentDefault);
        propertyRepository.delete(id);
    }

    @Override
    public Property getPropertyByKeyForCountry(String key, String countryIso) {
        Property property = propertyRepository.findByKeyAndCountry_IsoAlpha2Code(key, countryIso);
        return property != null ? property : propertyRepository.findByKeyAndCountryIsNull(key);
    }
}
