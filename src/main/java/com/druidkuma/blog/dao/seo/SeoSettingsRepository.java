package com.druidkuma.blog.dao.seo;

import com.druidkuma.blog.domain.entry.SeoSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 11/24/16
 */
@Repository
public interface SeoSettingsRepository extends JpaRepository<SeoSettings, Long> {
}
