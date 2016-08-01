package com.druidkuma.blog.web.dto;

import lombok.*;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/1/16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDetailedDto {
    private Long id;
    private String hexColor;
    private String nameKey;
}
