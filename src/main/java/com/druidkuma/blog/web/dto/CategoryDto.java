package com.druidkuma.blog.web.dto;

import lombok.*;

import java.time.Instant;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/29/16
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String nameKey;
    private String hexColor;
    private Integer numPosts;
    private Instant lastModified;
}
