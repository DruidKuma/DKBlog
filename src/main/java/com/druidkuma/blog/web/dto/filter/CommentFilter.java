package com.druidkuma.blog.web.dto.filter;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 10/17/16
 */
@Getter
@Setter
public class CommentFilter extends SimplePaginationFilter {
    private String typeFilter;
    private String ipFilter;
    private Long postFilter;
}
