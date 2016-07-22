package com.druidkuma.blog.dao.country;

import com.druidkuma.blog.domain.country.Country;
import com.druidkuma.blog.web.dto.CountryFlagRenderDto;
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
    List<Country> findAllByIsEnabledIsTrue();
    Country findByIsoAlpha2Code(String isoAlpha2Code);

    @Query(value = "SELECT new com.druidkuma.blog.web.dto.CountryFlagRenderDto(c.isoAlpha2Code, c.name) FROM Country c WHERE c.isEnabled = TRUE")
    List<CountryFlagRenderDto> getCountryDataForFlags();
}
