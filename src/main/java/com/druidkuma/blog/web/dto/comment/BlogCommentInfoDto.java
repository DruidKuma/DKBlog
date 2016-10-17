package com.druidkuma.blog.web.dto.comment;

import lombok.*;

import java.time.Instant;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 10/17/16
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogCommentInfoDto {
    private Long id;
    private String author;
    private String email;
    private String ipAddress;
    private Instant date;
    private String text;
    private String type;
    private Long postId;
    private Integer totalCommentsForPost;
}
