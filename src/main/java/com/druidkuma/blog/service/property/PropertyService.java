package com.druidkuma.blog.service.property;

import com.druidkuma.blog.domain.property.Property;

import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/15/16
 */
public interface PropertyService {
    List<Property> getPropertyListForCountry(String countryIso);
}
