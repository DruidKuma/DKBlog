package com.druidkuma.blog.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/19/16
 */
@Getter
@Setter
public class BlogPostFilter {
    private Integer currentPage;
    private Integer entriesOnPage;
    private String category;
    private String sort;
    private String search;
    private String filterPublished;
}
