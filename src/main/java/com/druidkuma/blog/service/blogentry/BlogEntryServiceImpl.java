package com.druidkuma.blog.service.blogentry;

import com.druidkuma.blog.dao.BlogEntryRepository;
import com.druidkuma.blog.domain.BlogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/13/16
 */
@Service
public class BlogEntryServiceImpl implements BlogEntryService {

    private BlogEntryRepository blogEntryRepository;

    @Autowired
    public BlogEntryServiceImpl(BlogEntryRepository blogEntryRepository) {
        this.blogEntryRepository = blogEntryRepository;
    }

    @Override
    public List<BlogEntry> getAll() {
        return blogEntryRepository.findAll();
    }
}
