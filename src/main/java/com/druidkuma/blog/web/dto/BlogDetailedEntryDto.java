package com.druidkuma.blog.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/18/16
 */
@Getter
@Setter
@Builder
public class BlogDetailedEntryDto {
    private Long id;
    private String title;
    private String permalink;
    private String captionSrc;
    private String author;
    private Instant creationDate;
    private String content;
    private Long numComments;
    private Boolean isPublished;
    private List<CountryFlagRenderDto> countries;
    private List<CategoryDto> categories;
}
