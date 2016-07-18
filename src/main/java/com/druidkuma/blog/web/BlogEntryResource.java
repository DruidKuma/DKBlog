package com.druidkuma.blog.web;

import com.druidkuma.blog.domain.BlogEntry;
import com.druidkuma.blog.domain.Comment;
import com.druidkuma.blog.service.blogentry.BlogEntryService;
import com.druidkuma.blog.web.dto.BlogCommentDto;
import com.druidkuma.blog.web.dto.BlogDetailedEntryDto;
import com.druidkuma.blog.web.dto.BlogEntryInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    @RequestMapping(method = RequestMethod.GET)
    public List<BlogEntryInfoDto> getAllEntries() {
        return blogEntryService.getAll().stream()
                .map(entry -> BlogEntryInfoDto.builder()
                        .id(entry.getId())
                        .views(entry.getNumViews())
                        .comments(entry.getNumComments())
                        .category("Test Category")
                        .title(entry.getContent().getTitle())
                        .status(entry.getIsPublished())
                        .imageUrl(entry.getContent().getImageUrl())
                        .creationDate(entry.getCreationDate())
                        .description(entry.getContent().getContents().substring(0, Math.min(entry.getContent().getContents().length(), 80)))
                        .build())
                .collect(Collectors.toList());
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

    private BlogCommentDto buildCommentDto(Comment comment) {
        return BlogCommentDto.builder()
                .author(comment.getAuthor())
                .body(comment.getBody())
                .creationDate(comment.getCreationDate())
                .id(comment.getId())
                .children(comment.getNestedComments().stream().map(this::buildCommentDto).collect(Collectors.toList()))
                .build();
    }
}
