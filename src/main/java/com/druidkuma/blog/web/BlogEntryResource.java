package com.druidkuma.blog.web;

import com.druidkuma.blog.service.blogentry.BlogEntryService;
import com.druidkuma.blog.web.dto.BlogEntryInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
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
                        .comments(100L)
                        .category("Test Category")
                        .title(entry.getContent().getTitle())
                        .status(entry.getIsPublished())
                        .imageUrl(entry.getContent().getImageUrl())
                        .creationDate(entry.getCreationDate())
                        .description(entry.getContent().getContents().substring(0, Math.min(entry.getContent().getContents().length(), 90)))
                        .build())
                .collect(Collectors.toList());
    }
}
