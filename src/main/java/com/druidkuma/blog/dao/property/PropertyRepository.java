package com.druidkuma.blog.dao.property;

import com.druidkuma.blog.domain.property.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/15/16
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findAllByCountry_IsoAlpha2Code(String countryIso2AlphaCode);
    List<Property> findAllByCountryIsNull();
}
