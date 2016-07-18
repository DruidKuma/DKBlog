package com.druidkuma.blog.service.blogentry;

import com.druidkuma.blog.domain.BlogEntry;

import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/13/16
 */
public interface BlogEntryService {
    List<BlogEntry> getAll();

    BlogEntry getOne(Long id);
}
