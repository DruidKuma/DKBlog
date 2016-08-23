package com.druidkuma.blog.web.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/26/16
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TranslatePanelDto {
    private List<String> groups;
    private Page<TPTranslation> translations;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TPTranslation {
        private String key;
        private String src;
        private String target;
        private Instant lastModified;
    }
}
