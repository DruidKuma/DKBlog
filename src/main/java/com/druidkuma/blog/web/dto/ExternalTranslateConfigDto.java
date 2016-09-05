package com.druidkuma.blog.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 9/5/16
 */
@Getter
@Setter
public class ExternalTranslateConfigDto {
    private String type;
    private CountryDto srcCountry;
    private CountryDto destCountry;
    private String group;
    private Boolean override;
}
