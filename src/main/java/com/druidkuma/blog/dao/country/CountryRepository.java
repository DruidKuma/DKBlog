package com.druidkuma.blog.dao.country;

import com.druidkuma.blog.domain.country.Country;
import com.druidkuma.blog.web.dto.country.CountryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/22/16
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByIsoAlpha2Code(String isoAlpha2Code);

    @Query(value = "SELECT new com.druidkuma.blog.web.dto.country.CountryDto(c.id, c.isoAlpha2Code, c.name, c.defaultLanguage.isoCode) FROM Country c WHERE c.isEnabled = TRUE ORDER BY c.name")
    List<CountryDto> getCountryData();

    @Query(value = "SELECT new com.druidkuma.blog.web.dto.country.CountryDto(c.id, c.isoAlpha2Code, c.name, c.defaultLanguage.isoCode) FROM Country c ORDER BY c.name")
    List<CountryDto> getAll();
}
