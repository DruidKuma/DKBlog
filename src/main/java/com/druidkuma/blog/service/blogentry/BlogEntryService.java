package com.druidkuma.blog.service.blogentry;

import com.druidkuma.blog.domain.BlogEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/13/16
 */
public interface BlogEntryService {
    BlogEntry getOne(Long id);
    Page<BlogEntry> getPageOfEntries(Pageable pageable, String filterPublished, String search, String categoryName);
}
