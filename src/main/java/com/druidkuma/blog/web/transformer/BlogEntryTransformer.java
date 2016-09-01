package com.druidkuma.blog.web.transformer;

import com.druidkuma.blog.dao.blogEntry.BlogEntryRepository;
import com.druidkuma.blog.domain.BlogEntry;
import com.druidkuma.blog.domain.Content;
import com.druidkuma.blog.domain.category.Category;
import com.druidkuma.blog.domain.country.Country;
import com.druidkuma.blog.web.dto.BlogDetailedEntryDto;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/11/16
 */
@Component
public class BlogEntryTransformer implements DtoTransformer<BlogEntry, BlogDetailedEntryDto> {

    private CountryTransformer countryTransformer;
    private CategoryTransformer categoryTransformer;
    private BlogEntryRepository blogEntryRepository;

    @Autowired
    public BlogEntryTransformer(CountryTransformer countryTransformer, CategoryTransformer categoryTransformer, BlogEntryRepository blogEntryRepository) {
        this.countryTransformer = countryTransformer;
        this.categoryTransformer = categoryTransformer;
        this.blogEntryRepository = blogEntryRepository;
    }

    @Override
    public BlogEntry transformFromDto(BlogDetailedEntryDto dto) {
        BlogEntry blogEntry = dto.getId() != null ? blogEntryRepository.findOne(dto.getId()) : new BlogEntry();
        if(dto.getId() == null) {
            blogEntry.setNumComments(0L);
            blogEntry.setNumViews(0L);
            blogEntry.setCategories(Lists.newArrayList());
            blogEntry.setCountries(Lists.newArrayList());
            blogEntry.setCreationDate(Instant.now());
        }
        blogEntry.setAuthor(dto.getAuthor());
        blogEntry.setIsPublished(dto.getIsPublished());
        blogEntry.setCommentsEnabled(dto.getIsCommentEnabled());
        blogEntry.setPermalink(dto.getPermalink());

        blogEntry.getCategories().clear();
        for (Category category : dto.getCategories().stream()
                .map(categoryDto -> categoryTransformer.transformFromDto(categoryDto))
                .collect(Collectors.toList())) {
            blogEntry.getCategories().add(category);
        }

        blogEntry.getCountries().clear();
        for (Country country : dto.getCountries().stream()
                .map(countryDto -> countryTransformer.transformFromDto(countryDto))
                .collect(Collectors.toList())) {
            blogEntry.getCountries().add(country);
        }

        Content content = blogEntry.getContent() == null ? new Content() : blogEntry.getContent();
        content.setContents(dto.getContent());
        content.setImageUrl(dto.getCaptionSrc());
        content.setTitle(dto.getTitle());
        blogEntry.setContent(content);

        return blogEntry;
    }

    @Override
    public BlogDetailedEntryDto tranformToDto(BlogEntry entry) {
        return BlogDetailedEntryDto.builder()
                .title(entry.getContent().getTitle())
                .permalink(entry.getPermalink())
                .captionSrc(entry.getContent().getImageUrl())
                .creationDate(entry.getCreationDate())
                .author(entry.getAuthor())
                .content(entry.getContent().getContents())
                .numComments(entry.getNumComments())
                .isPublished(entry.getIsPublished())
                .isCommentEnabled(entry.getCommentsEnabled())
                .countries(entry.getCountries().stream().map(country -> countryTransformer.tranformToDto(country)).collect(Collectors.toList()))
                .categories(entry.getCategories().stream().map(category -> categoryTransformer.tranformToDto(category)).collect(Collectors.toList()))
                .id(entry.getId())
                .build();
    }
}
