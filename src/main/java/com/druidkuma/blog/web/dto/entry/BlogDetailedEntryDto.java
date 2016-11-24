package com.druidkuma.blog.web.dto.entry;

import com.druidkuma.blog.domain.entry.SeoSettings;
import com.druidkuma.blog.web.dto.category.CategoryDto;
import com.druidkuma.blog.web.dto.country.CountryDto;
import lombok.*;

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
@NoArgsConstructor
@AllArgsConstructor
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
    private Boolean isCommentEnabled;
    private List<CountryDto> countries;
    private List<CategoryDto> categories;
    private Long previousId;
    private Long nextId;
    private SeoSettings seoSettings;
}
