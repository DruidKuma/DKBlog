package com.druidkuma.blog.web.dto.filter;

import lombok.*;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/19/16
 */
@Getter
@Setter
public class BlogPostFilter extends SimplePaginationFilter {
    private String category;
    private String filterPublished;
}
