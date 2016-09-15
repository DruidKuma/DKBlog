package com.druidkuma.blog.web.dto.country;

import lombok.*;

import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 9/7/16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryConfigDto {
    private String isoCode;
    private String name;
    private Boolean enabled;
    private LanguageDto defaultLanguage;
    private List<LanguageDto> languages;
}
