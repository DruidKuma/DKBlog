package com.druidkuma.blog.service.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

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

    private PropertySource propertySource;

    public String getProperty(String key) {
        return String.valueOf(propertySource.getProperty(key));
    }

    @PostConstruct
    public void fillPropertiesFromFile() throws IOException {
        for (Resource location : locations) {
            propertySource = new YamlPropertySourceLoader().load("file", location, null);
        }
    }
}
