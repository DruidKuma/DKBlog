package com.druidkuma.blog.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
@Builder
public class BlogCommentDto {
    private Integer id;
    private String author;
    private String body;
    private Instant creationDate;
    private List<BlogCommentDto> children;
}
