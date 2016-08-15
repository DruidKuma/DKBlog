package com.druidkuma.blog.web.dto;

import lombok.*;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/22/16
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryDto {
    private Long id;
    private String isoCode;
    private String name;
    private String defaultLanguageIso;
}
