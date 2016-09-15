package com.druidkuma.blog.web;

import com.druidkuma.blog.domain.BlogEntry;
import com.druidkuma.blog.exception.PermalinkExistsException;
import com.druidkuma.blog.service.blogentry.BlogEntryService;
import com.druidkuma.blog.service.permalink.PermalinkGenerationService;
import com.druidkuma.blog.util.procedures.ProcedureService;
import com.druidkuma.blog.web.dto.entry.BlogDetailedEntryDto;
import com.druidkuma.blog.web.dto.entry.BlogEntryInfoDto;
import com.druidkuma.blog.web.dto.filter.BlogPostFilter;
import com.druidkuma.blog.web.dto.country.CountryDto;
import com.druidkuma.blog.web.transformer.BlogEntryTransformer;
import com.druidkuma.blog.web.transformer.CategoryTransformer;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
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
    private BlogEntryTransformer blogEntryTransformer;
    private CategoryTransformer categoryTransformer;
    private PermalinkGenerationService permalinkGenerationService;
    private ProcedureService procedureService;

    @Autowired
    public BlogEntryResource(BlogEntryService blogEntryService, BlogEntryTransformer blogEntryTransformer, CategoryTransformer categoryTransformer, PermalinkGenerationService permalinkGenerationService, ProcedureService procedureService) {
        this.blogEntryService = blogEntryService;
        this.blogEntryTransformer = blogEntryTransformer;
        this.categoryTransformer = categoryTransformer;
        this.permalinkGenerationService = permalinkGenerationService;
        this.procedureService = procedureService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BlogDetailedEntryDto getEntry(@PathVariable("id") Long id,
                                         @CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {
        BlogEntry entry = blogEntryService.getOne(id);
        if(entry == null) return new BlogDetailedEntryDto();
        BlogDetailedEntryDto dto = blogEntryTransformer.tranformToDto(entry);
        Pair<Long, Long> shiftedEntries = procedureService.getPreviousAndNextBlogEntryIds(id, currentCountryIso);
        dto.setPreviousId(shiftedEntries.getLeft());
        dto.setNextId(shiftedEntries.getRight());
        return dto;
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
                        .categories(entry.getCategories().stream()
                                .map(category -> categoryTransformer.tranformToSimpleDto(category))
                                .collect(Collectors.toList()))
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
    public Map<String, String> generatePermalink(@RequestBody BlogEntryInfoDto blogInfoDto,
                                                 @CookieValue(value = "currentCountryIso", defaultValue = "US") String currentCountryIso) {
        return Collections.singletonMap("permalink",
                permalinkGenerationService.generatePermalink(
                        blogInfoDto.getTitle(),
                        blogInfoDto.getId(),
                        blogInfoDto.getCreationDate() == null ? Instant.now() : blogInfoDto.getCreationDate(),
                        currentCountryIso));
    }

    @RequestMapping(method = RequestMethod.POST)
    public BlogDetailedEntryDto saveBlogEntry(@RequestBody BlogDetailedEntryDto blogEntryDto) {
        if(blogEntryService.permalinkExists(
                blogEntryDto.getPermalink(),
                blogEntryDto.getCountries().stream().map(CountryDto::getIsoCode).collect(Collectors.toList()),
                blogEntryDto.getId())) {
            throw new PermalinkExistsException();
        }
        blogEntryDto.setId(blogEntryService.saveBlogEntry(blogEntryTransformer.transformFromDto(blogEntryDto)).getId());
        return blogEntryDto;
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
