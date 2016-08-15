package com.druidkuma.blog.web.dto;

import lombok.*;

import java.util.List;

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
    private String textColor;
    private String nameKey;
    private List<CountryFlagRenderDto> countries;
    private List<TranslationDto> translations;
}
