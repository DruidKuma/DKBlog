package com.druidkuma.blog.dao.property;

import com.druidkuma.blog.domain.property.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    Property findByKeyAndCountryIsNull(String key);
    Property findByKeyAndCountry_IsoAlpha2Code(String key, String isoCode);
    List<Property> findAllByCountryIsNull();
    List<Property> findAllByCountry_IsoAlpha2Code(String isoCode);

    @Query(value = "SELECT pr FROM Property pr LEFT OUTER JOIN Country c ON pr.country.id = c.id WHERE " +
           "(EXISTS (SELECT pr2 FROM Property pr2 JOIN Country c2 ON pr2.country.id = c2.id " +
           "            WHERE pr2.key = pr.key AND c2.isoAlpha2Code = :countryIso) AND c.isoAlpha2Code = :countryIso) " +
           "OR " +
           "(NOT EXISTS (SELECT pr2 FROM Property pr2 JOIN Country c3 ON pr2.country.id = c3.id " +
            "           WHERE pr2.key = pr.key AND c3.isoAlpha2Code = :countryIso) AND c IS NULL)")
    List<Property> findAllForCountryOrDefault(@Param("countryIso") String countryIso);
}
