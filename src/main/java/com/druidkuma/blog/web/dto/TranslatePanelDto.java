package com.druidkuma.blog.web.dto;

import com.druidkuma.blog.domain.i18n.Translation;
import lombok.*;

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
    private Long groupId;
    private String displayName;
    private List<String> childGroupNames;
    private List<Translation> translations;
}
