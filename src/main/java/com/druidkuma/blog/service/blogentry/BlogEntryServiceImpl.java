package com.druidkuma.blog.service.blogentry;

import com.druidkuma.blog.dao.blogEntry.BlogEntryRepository;
import com.druidkuma.blog.dao.blogEntry.specification.BlogEntrySpecification;
import com.druidkuma.blog.dao.blogEntry.specification.SearchCriteria;
import com.druidkuma.blog.domain.BlogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public BlogEntry getOne(Long id) {
        return blogEntryRepository.findOne(id);
    }

    @Override
    public Page<BlogEntry> getPageOfEntries(Pageable pageable, String filterPublished, String search, String categoryName) {
        return blogEntryRepository.findAll(new BlogEntrySpecification(new SearchCriteria(search, filterPublished, categoryName)), pageable);
    }

    @Override
    public void switchPublishStatus(Long id) {
        BlogEntry entry = blogEntryRepository.findOne(id);
        entry.setIsPublished(!entry.getIsPublished());
        blogEntryRepository.saveAndFlush(entry);
    }

    @Override
    public void deleteBlogPost(Long id) {
        blogEntryRepository.delete(id);
    }
}
