package com.druidkuma.blog.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class CountryFlagRenderDto {
    private Long id;
    private String isoCode;
    private String name;
    private String defaultLanguageIso;

    public CountryFlagRenderDto(Long id, String isoCode, String name) {
        this.id = id;
        this.isoCode = isoCode;
        this.name = name;
    }
}
