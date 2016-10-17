package com.druidkuma.blog.web.dto.comment;

import com.google.common.collect.Lists;
import lombok.*;

import java.time.Instant;
import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/18/16
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogCommentDto {
    private Long id;
    private Long blogPostId;
    private Long parentId;
    private String author;
    private String body;
    private Instant creationDate;
    private List<BlogCommentDto> children = Lists.newArrayList();
}
