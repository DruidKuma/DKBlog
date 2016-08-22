package com.druidkuma.blog.service.property;

import com.druidkuma.blog.exception.UnknownPropertySourceException;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/19/16
 */
@Component
public class FilePropertiesHolder {

    @Value("#{'${properties.filenames}'.split(',')}")
    private Resource[] locations;

    private PropertySources propertySources;

    public String getProperty(String key) {
        for (PropertySource<?> propertySource : propertySources) {
            if(propertySource.containsProperty(key)) {
                return String.valueOf(propertySource.getProperty(key));
            }
        }
        return null;
    }

    @PostConstruct
    public void fillPropertiesFromFile() throws IOException {
        MutablePropertySources sources = new MutablePropertySources();
        for (Resource location : locations) {
            if (location.getFilename().endsWith(".yml") || location.getFilename().endsWith(".yaml")) {
                sources.addLast(new YamlPropertySourceLoader().load(location.getFilename(), location, null));
            }
            else if (location.getFilename().endsWith(".properties")) {
                sources.addLast(new PropertiesPropertySourceLoader().load(location.getFilename(), location, null));
            }
            else throw new UnknownPropertySourceException(location.getFilename());
        }
        propertySources = sources;
    }

    public Map<String, Object> getProperties() {
        Map<String, Object> result = Maps.newHashMap();
        for (PropertySource<?> propertySource : propertySources) {
            if (propertySource instanceof MapPropertySource) {
                result.putAll(((MapPropertySource) propertySource).getSource());
            }
        }
        return result;
    }
}
