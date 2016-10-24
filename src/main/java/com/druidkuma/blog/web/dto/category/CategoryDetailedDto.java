package com.druidkuma.blog.web.dto.category;

import com.druidkuma.blog.web.dto.country.CountryDto;
import com.druidkuma.blog.web.dto.i18n.TranslationDto;
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
    private List<CountryDto> countries;
    private List<TranslationDto> translations;
}