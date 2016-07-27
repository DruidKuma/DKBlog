package com.druidkuma.blog.service.blogentry;

import com.druidkuma.blog.dao.blogEntry.BlogEntryRepository;
import com.druidkuma.blog.dao.blogEntry.specification.BlogEntrySpecification;
import com.druidkuma.blog.dao.blogEntry.specification.SearchCriteria;
import com.druidkuma.blog.dao.country.CountryRepository;
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
    private CountryRepository countryRepository;

    @Autowired
    public BlogEntryServiceImpl(BlogEntryRepository blogEntryRepository, CountryRepository countryRepository) {
        this.blogEntryRepository = blogEntryRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public BlogEntry getOne(Long id) {
        return blogEntryRepository.findOne(id);
    }

    @Override
    public Page<BlogEntry> getPageOfEntries(Pageable pageable, String filterPublished, String search, String categoryName, String currentCountryIso) {
        return blogEntryRepository.findAll(new BlogEntrySpecification(
                new SearchCriteria(search,
                        filterPublished,
                        categoryName,
                        countryRepository.findByIsoAlpha2Code(currentCountryIso))), pageable);
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
