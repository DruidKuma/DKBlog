package com.druidkuma.blog.service.blogentry;

import com.druidkuma.blog.dao.BlogEntryRepository;
import com.druidkuma.blog.domain.BlogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Override
    public BlogEntry getOne(Long id) {
        return blogEntryRepository.findOne(id);
    }

    @Override
    public Page<BlogEntry> getPageOfEntries(Pageable pageable, Boolean publishFilter, String search) {
        if(publishFilter != null) {
            return blogEntryRepository.findAllByIsPublished(publishFilter, pageable);
        }
        return blogEntryRepository.findAll(pageable);
    }
}
