package com.druidkuma.blog.web.dto.i18n;

import lombok.*;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExportConfigDto {
    private String targetCountry;
    private String groupName;
    private String columnSeparator;
    private String rowSeparator;
}
