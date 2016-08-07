package com.druidkuma.blog.web;

import com.druidkuma.blog.domain.BlogEntry;
import com.druidkuma.blog.service.blogentry.BlogEntryService;
import com.druidkuma.blog.util.NormalizationUtil;
import com.druidkuma.blog.web.dto.BlogDetailedEntryDto;
import com.druidkuma.blog.web.dto.BlogEntryInfoDto;
import com.druidkuma.blog.web.dto.BlogPostFilter;
import com.druidkuma.blog.web.transformer.CategoryTransformer;
import com.druidkuma.blog.web.transformer.CountryTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/13/16
 */
@RestController
@RequestMapping("/api/blog/entry")
public class BlogEntryResource {

    private BlogEntryService blogEntryService;
    private CountryTransformer countryTransformer;
    private CategoryTransformer categoryTransformer;

    @Autowired
    public BlogEntryResource(BlogEntryService blogEntryService, CountryTransformer countryTransformer, CategoryTransformer categoryTransformer) {
        this.blogEntryService = blogEntryService;
        this.countryTransformer = countryTransformer;
        this.categoryTransformer = categoryTransformer;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BlogDetailedEntryDto getEntry(@PathVariable("id") Long id) {

        BlogEntry entry = blogEntryService.getOne(id);

        return BlogDetailedEntryDto.builder()
                .title(entry.getContent().getTitle())
                .permalink(entry.getPermalink())
                .creationDate(entry.getCreationDate())
                .author(entry.getAuthor())
                .content(entry.getContent().getContents())
                .numComments(entry.getNumComments())
                .isPublished(entry.getIsPublished())
                .countries(entry.getCountries().stream().map(country -> countryTransformer.tranformToDto(country)).collect(Collectors.toList()))
                .categories(entry.getCategories().stream().map(category -> categoryTransformer.tranformToDto(category)).collect(Collectors.toList()))
                .id(entry.getId()).build();
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<BlogEntryInfoDto> getPageOfBlogEntries(BlogPostFilter filter,
                                                       @CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {

        Pageable pageable = buildPageRequest(filter);
        Page<BlogEntry> pageOfEntries = blogEntryService.getPageOfEntries(
                pageable,
                filter.getFilterPublished(),
                filter.getSearch(),
                filter.getCategory(), currentCountryIso);

        return new PageImpl<>(pageOfEntries.getContent().stream()
                .map(entry -> BlogEntryInfoDto.builder()
                        .id(entry.getId())
                        .views(entry.getNumViews())
                        .comments(entry.getNumComments())
                        .category("Test Category")
                        .title(entry.getContent().getTitle())
                        .status(entry.getIsPublished())
                        .imageUrl(entry.getContent().getImageUrl())
                        .creationDate(entry.getCreationDate())
                        .build())
                .collect(Collectors.toList()), pageable, pageOfEntries.getTotalElements());
    }

    @RequestMapping(value = "/publish/{id}", method = RequestMethod.PUT)
    public void switchPublishStatus(@PathVariable("id") Long id) {
        blogEntryService.switchPublishStatus(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteBlogPost(@PathVariable("id") Long id) {
        blogEntryService.deleteBlogPost(id);
    }

    @RequestMapping(value = "/permalink", method = RequestMethod.POST)
    public Map<String, String> generatePermalink(@RequestBody String blogTitle) {
        return Collections.singletonMap("permalink", NormalizationUtil.normalizeUrlNameKey(blogTitle));
    }

    private Pageable buildPageRequest(BlogPostFilter blogPostFilter) {
        Integer page = blogPostFilter.getCurrentPage() - 1;
        Integer pageSize = blogPostFilter.getEntriesOnPage();

        String[] sort;
        if(blogPostFilter.getSort() == null || blogPostFilter.getSort().split(" ").length != 2) {
            sort = "creationDate DESC".split(" ");
        }
        else {
            sort = blogPostFilter.getSort().split(" ");
        }
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        return new PageRequest(page, pageSize, direction, sort[0]);
    }
}
