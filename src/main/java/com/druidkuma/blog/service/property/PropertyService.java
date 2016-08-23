package com.druidkuma.blog.service.property;

import com.druidkuma.blog.domain.property.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/15/16
 */
public interface PropertyService {
    List<Property> getPropertyListForCountry(String countryIso);
    Page<Property> getPropertyPageForCountry(Pageable pageable, String search, String currentCountryIso);

    void saveProperty(Property property);
    void deleteProperty(Long id);
    void makeDefaultPropertyById(Long id);
    Map<String,Object> getAllFileProperties();
    Map<String,String> getAllSystemProperties();

    /**
     * Property object
     */
    Property getProperty(String key);
    Property getProperty(String key, String countryIso);
    Property getProperty(String key, String countryIso, Property defaultValue);

    /**
     * Property List
     */
    List<Property> getPropertyList();
    List<Property> getPropertyList(String countryIso);

    /**
     * String
     */
    String getString(String key);
    String getString(String key, String countryIso);
    String getString(String key, String countryIsoCode, String defaultValue);

    /**
     * Multiple values comma separated (or with given separator)
     */
    List<String> getStringList(String key);
    List<String> getStringList(String key, String countryIso);
    List<String> getStringList(String key, String countryIso, String separator);
    List<String> getStringList(String key, String countryIso, List<String> defaultValue);
    List<String> getStringList(String key, String countryIso, String separator, List<String> defaultValue);

    /**
     * Integer
     */
    Integer getInteger(String key);
    Integer getInteger(String key, Integer defaultValue);
    Integer getInteger(String key, String countryIso);
    Integer getInteger(String key, String countryIso, Integer defaultValue);

    /**
     * Long
     */
    Long getLong(String key);
    Long getLong(String key, Long defaultValue);
    Long getLong(String key, String countryIso);
    Long getLong(String key, String countryIso, Long defaultValue);

    /**
     * Double
     */
    Double getDouble(String key);
    Double getDouble(String key, Double defaultValue);
    Double getDouble(String key, String countryIso);
    Double getDouble(String key, String countryIso, Double defaultValue);

    /**
     * Boolean
     */
    Boolean getBoolean(String key);
    Boolean getBoolean(String key, Boolean defaultValue);
    Boolean getBoolean(String key, String countryIso);
    Boolean getBoolean(String key, String countryIso, Boolean defaultValue);

    /**
     * Parses property in map format, e.g. "key1::value1,key2::value2"
     */
    Map<String, String> getStringMap(String key);
    Map<String, String> getStringMap(String key, Map<String, String> defaultValue, String separator);
    Map<String, String> getStringMap(String key, String countryIso);
    Map<String, String> getStringMap(String key, String countryIso, Map<String, String> defaultValue, String separator);

    /**
     * Integer Set
     */
    Set<Integer> getIntegerSet(String key);
    Set<Integer> getIntegerSet(String key, String countryIso);
    Set<Integer> getIntegerSet(String key, String countryIso, String separator);
    Set<Integer> getIntegerSet(String key, String countryIso, Set<Integer> defaultValue);
    Set<Integer> getIntegerSet(String key, String countryIso, String separator, Set<Integer> defaultValue);

    /**
     * JSON custom type
     */
    <T> T getJSON(String key, Class<T> type);
    <T> T getJSON(String key, Class<T> type, T defaultValue);
    <T> T getJSON(String key, String countryIso, Class<T> type);
    <T> T getJSON(String key, String countryIso, Class<T> type, T defaultValue);
}
