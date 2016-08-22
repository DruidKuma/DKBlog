package com.druidkuma.blog.web.dto;

import lombok.*;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/22/16
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewTranslationDto {
    private String group;
    private String key;
    private String value;
    private String countryIso;
}
