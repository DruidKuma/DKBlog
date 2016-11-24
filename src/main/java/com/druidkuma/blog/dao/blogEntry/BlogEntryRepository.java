package com.druidkuma.blog.dao.blogEntry;

import com.druidkuma.blog.domain.entry.BlogEntry;
import com.druidkuma.blog.domain.country.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/13/16
 */
@Repository
public interface BlogEntryRepository extends JpaRepository<BlogEntry, Long>, JpaSpecificationExecutor<BlogEntry> {

    @Query(value = "SELECT be FROM BlogEntry be WHERE be.permalink = :permalink AND :country MEMBER OF be.countries")
    BlogEntry findByPermalinkInCountry(@Param("permalink") String  permalink,
                                       @Param("country") Country country);
}
