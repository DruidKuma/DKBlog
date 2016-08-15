package com.druidkuma.blog.web.dto;

import lombok.*;

import java.time.Instant;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/15/16
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyDto {
    private Long id;
    private String key;
    private String value;
    private Instant lastModified;
    private CountryDto country;
}
