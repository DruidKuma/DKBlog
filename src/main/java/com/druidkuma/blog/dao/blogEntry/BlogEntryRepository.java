package com.druidkuma.blog.dao.blogEntry;

import com.druidkuma.blog.domain.BlogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
    BlogEntry findByPermalink(String  permalink);
}
