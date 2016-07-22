package com.druidkuma.blog.dao.country;

import com.druidkuma.blog.domain.country.Country;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
