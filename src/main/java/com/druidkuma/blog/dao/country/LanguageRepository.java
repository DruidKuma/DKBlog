package com.druidkuma.blog.dao.country;

import com.druidkuma.blog.domain.country.Language;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
