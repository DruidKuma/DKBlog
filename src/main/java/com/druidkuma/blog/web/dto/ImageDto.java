package com.druidkuma.blog.web.dto;

import lombok.*;

import java.time.Instant;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/4/16
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDto {
    private Long id;
    private String fullImgSrc;
    private String thumbImgSrc;
    private Instant createdAt;
}
