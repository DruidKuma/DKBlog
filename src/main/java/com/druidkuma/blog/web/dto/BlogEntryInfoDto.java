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
 * @since 7/13/16
 */
@Getter
@Setter
@Builder
public class BlogEntryInfoDto {
    private Long id;
    private String title;
    private Long views;
    private long comments;
    private Boolean status;
    private List<CategoryDto> categories;
    private String imageUrl;
    private Instant creationDate;
}
