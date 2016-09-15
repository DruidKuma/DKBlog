package com.druidkuma.blog.web.dto.entry;

import com.druidkuma.blog.web.dto.category.CategoryDto;
import lombok.*;

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
@NoArgsConstructor
@AllArgsConstructor
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
