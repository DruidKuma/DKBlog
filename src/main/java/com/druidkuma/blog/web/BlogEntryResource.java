package com.druidkuma.blog.web;

import com.druidkuma.blog.domain.BlogEntry;
import com.druidkuma.blog.domain.Comment;
import com.druidkuma.blog.service.blogentry.BlogEntryService;
import com.druidkuma.blog.web.dto.BlogCommentDto;
import com.druidkuma.blog.web.dto.BlogDetailedEntryDto;
import com.druidkuma.blog.web.dto.BlogEntryInfoDto;
import com.druidkuma.blog.web.dto.BlogPostFilter;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    public BlogEntryResource(BlogEntryService blogEntryService) {
        this.blogEntryService = blogEntryService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BlogDetailedEntryDto getEntry(@PathVariable("id") Long id) {

        BlogEntry entry = blogEntryService.getOne(id);

        return BlogDetailedEntryDto.builder()
                .title(entry.getContent().getTitle())
                .creationDate(entry.getCreationDate())
                .author(entry.getAuthor())
                .content(entry.getContent().getContents())
                .numComments(entry.getNumComments())
                .comments(entry.getComments().stream().map(this::buildCommentDto).collect(Collectors.toList()))
                .id(entry.getId()).build();
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<BlogEntryInfoDto> getPageOfBlogEntries(BlogPostFilter filter) {

        Pageable pageable = buildPageRequest(filter);
        Page<BlogEntry> pageOfEntries = blogEntryService.getPageOfEntries(
                pageable,
                filter.getFilterPublished(),
                filter.getSearch(),
                filter.getCategory());

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
                        .description(Jsoup.parse(entry.getContent()
                                .getContents()).text()
                                .substring(0, Math.min(entry.getContent()
                                        .getContents().length(), 80)))
                        .build())
                .collect(Collectors.toList()), pageable, pageOfEntries.getTotalElements());
    }

    private BlogCommentDto buildCommentDto(Comment comment) {
        return BlogCommentDto.builder()
                .author(comment.getAuthor())
                .body(comment.getBody())
                .creationDate(comment.getCreationDate())
                .id(comment.getId())
                .children(comment.getNestedComments().stream().map(this::buildCommentDto).collect(Collectors.toList()))
                .build();
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
