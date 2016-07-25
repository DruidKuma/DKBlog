package com.druidkuma.blog.dao.country;

import com.druidkuma.blog.domain.country.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/22/16
 */
@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    Language findByIsoCode(String isoCode);

    @Query(value = "SELECT l.l_id FROM language l JOIN country_2_language c2l ON l.l_id = c2l.c2l_language_id WHERE c2l.c2l_country_id = :countryId AND c2l.c2l_is_default", nativeQuery = true)
    Long getDefaultLanguageForCountry(@Param("countryId") Long id);
}
