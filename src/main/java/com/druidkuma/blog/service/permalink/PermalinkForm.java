package com.druidkuma.blog.service.permalink;

import lombok.*;

import java.time.Instant;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/19/16
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermalinkForm {
    private Long id;
    private Instant date;
    private String title;
}
