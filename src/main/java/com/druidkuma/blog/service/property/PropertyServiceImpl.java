package com.druidkuma.blog.service.property;

import com.druidkuma.blog.dao.property.PropertyRepository;
import com.druidkuma.blog.domain.property.Property;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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

    private String extractValue(Property property) {
        return property == null ? null : property.getValue();
    }

    @Override
    public List<Property> getPropertyListForCountry(String countryIso) {
        return propertyRepository.findAllForCountryOrDefault(countryIso);
    }

    @Override
    public void saveProperty(Property property) {
        if(property.getCountry() != null && getBoolean(CommonKeys.AUTO_CREATE_DEFAULT) && getProperty(property.getKey()) == null) {
            propertyRepository.saveAndFlush(Property.builder()
                    .key(property.getKey())
                    .value(property.getValue())
                    .lastModified(Instant.now())
                    .build());
        }
        propertyRepository.saveAndFlush(property);
    }

    @Override
    public void deleteProperty(Long id) {
        Property property = propertyRepository.findOne(id);
        if(property.getCountry() != null && getBoolean(CommonKeys.AUTO_DELETE_DEFAULT)) {
            Property defaultProperty = getProperty(property.getKey());
            if(defaultProperty != null) propertyRepository.delete(defaultProperty);
        }
        propertyRepository.delete(id);
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
    public Property getProperty(String key) {
        return propertyRepository.findByKeyAndCountryIsNull(key);
    }

    @Override
    public Property getProperty(String key, String countryIso) {
        return getProperty(key, countryIso, getProperty(key));
    }

    @Override
    public Property getProperty(String key, String countryIso, Property defaultValue) {
        Property property = propertyRepository.findByKeyAndCountry_IsoAlpha2Code(key, countryIso);
        return property != null ? property : defaultValue;
    }

    @Override
    public List<Property> getPropertyList() {
        return propertyRepository.findAllByCountryIsNull();
    }

    @Override
    public List<Property> getPropertyList(String countryIso) {
        return propertyRepository.findAllByCountry_IsoAlpha2Code(countryIso);
    }

    @Override
    public String getString(String key) {
        return extractValue(getProperty(key));
    }

    @Override
    public String getString(String key, String countryIso) {
        return extractValue(getProperty(key, countryIso));
    }

    @Override
    public String getString(String key, String countryIsoCode, String defaultValue) {
        String value = getString(key, countryIsoCode);
        return value == null ? defaultValue : value;
    }

    @Override
    public List<String> getStringList(String key) {
        return getStringList(key, null);
    }

    @Override
    public List<String> getStringList(String key, String countryIso) {
        return getStringList(key, countryIso, null, null);
    }

    @Override
    public List<String> getStringList(String key, String countryIso, String separator) {
        return getStringList(key, countryIso, separator, null);
    }

    @Override
    public List<String> getStringList(String key, String countryIso, List<String> defaultValue) {
        return getStringList(key, countryIso, null, defaultValue);
    }

    @Override
    public List<String> getStringList(String key, String countryIso, String separator, List<String> defaultValue) {
        String value = countryIso == null ? extractValue(getProperty(key)) : extractValue(getProperty(key, countryIso));
        if(separator == null) separator = ",";
        return value == null ? defaultValue : Arrays.asList(value.split(separator));
    }

    @Override
    public Integer getInteger(String key) {
        return getInteger(key, null, null);
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        return getInteger(key, null, defaultValue);
    }

    @Override
    public Integer getInteger(String key, String countryIso) {
        return getInteger(key, countryIso, null);
    }

    @Override
    public Integer getInteger(String key, String countryIso, Integer defaultValue) {
        String value = countryIso == null ? extractValue(getProperty(key)) : extractValue(getProperty(key, countryIso));
        return value == null ? defaultValue : Integer.valueOf(value);
    }

    @Override
    public Long getLong(String key) {
        return getLong(key, null, null);
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        return getLong(key, null, defaultValue);
    }

    @Override
    public Long getLong(String key, String countryIso) {
        return getLong(key, countryIso, null);
    }

    @Override
    public Long getLong(String key, String countryIso, Long defaultValue) {
        String value = countryIso == null ? extractValue(getProperty(key)) : extractValue(getProperty(key, countryIso));
        return value == null ? defaultValue : Long.valueOf(value);
    }

    @Override
    public Double getDouble(String key) {
        return getDouble(key, null, null);
    }

    @Override
    public Double getDouble(String key, Double defaultValue) {
        return getDouble(key, null, defaultValue);
    }

    @Override
    public Double getDouble(String key, String countryIso) {
        return getDouble(key, countryIso, null);
    }

    @Override
    public Double getDouble(String key, String countryIso, Double defaultValue) {
        String value = countryIso == null ? extractValue(getProperty(key)) : extractValue(getProperty(key, countryIso));
        return value == null ? defaultValue : Double.valueOf(value);
    }

    @Override
    public Boolean getBoolean(String key) {
        return getBoolean(key, null, null);
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return getBoolean(key, null, defaultValue);
    }

    @Override
    public Boolean getBoolean(String key, String countryIso) {
        return getBoolean(key, countryIso, null);
    }

    @Override
    public Boolean getBoolean(String key, String countryIso, Boolean defaultValue) {
        String value = countryIso == null ? extractValue(getProperty(key)) : extractValue(getProperty(key, countryIso));
        return value == null ? defaultValue : Boolean.valueOf(value);
    }

    @Override
    public Map<String, String> getStringMap(String key) {
        return getStringMap(key, null, null, null);
    }

    @Override
    public Map<String, String> getStringMap(String key, Map<String, String> defaultValue, String separator) {
        return getStringMap(key, null, defaultValue, separator);
    }

    @Override
    public Map<String, String> getStringMap(String key, String countryIso) {
        return getStringMap(key, countryIso, null, null);
    }

    @Override
    public Map<String, String> getStringMap(String key, String countryIso, Map<String, String> defaultValue, String separator) {
        if (separator == null) separator = ",";
        String value = countryIso == null ? extractValue(getProperty(key)) : extractValue(getProperty(key, countryIso));
        if(value == null) return defaultValue;

        Map<String, String> stringMap = Maps.newHashMap();
        for (String split : value.split(separator)) {
            String[] key2Value = split.split("::");
            Preconditions.checkArgument(key2Value.length == 2, "invalid property map format: " + value);
            stringMap.put(key2Value[0], key2Value[1]);
        }
        return stringMap;
    }

    @Override
    public Set<Integer> getIntegerSet(String key) {
        return getIntegerSet(key, null, null, null);
    }

    @Override
    public Set<Integer> getIntegerSet(String key, String countryIso) {
        return getIntegerSet(key, countryIso, null, null);
    }

    @Override
    public Set<Integer> getIntegerSet(String key, String countryIso, String separator) {
        return getIntegerSet(key, countryIso, separator, null);
    }

    @Override
    public Set<Integer> getIntegerSet(String key, String countryIso, Set<Integer> defaultValue) {
        return getIntegerSet(key, countryIso, null, defaultValue);
    }

    @Override
    public Set<Integer> getIntegerSet(String key, String countryIso, String separator, Set<Integer> defaultValue) {
        List<String> stringValues = getStringList(key, countryIso, separator, Collections.emptyList());
        return stringValues.isEmpty() ? defaultValue : stringValues.stream()
                .filter(StringUtils::isNumeric)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    @Override
    public <T> T getJSON(String key, Class<T> type) {
        return getJSON(key, null, type, null);
    }

    @Override
    public <T> T getJSON(String key, Class<T> type, T defaultValue) {
        return getJSON(key, null, type, defaultValue);
    }

    @Override
    public <T> T getJSON(String key, String countryIso, Class<T> type) {
        return getJSON(key, countryIso, type, null);
    }

    @Override
    @SneakyThrows
    public <T> T getJSON(String key, String countryIso, Class<T> type, T defaultValue) {
        String jsonString = countryIso == null ? extractValue(getProperty(key)) : extractValue(getProperty(key, countryIso));
        return jsonString == null ? defaultValue : new ObjectMapper().readValue(jsonString, type);
    }
}
