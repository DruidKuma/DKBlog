package com.druidkuma.blog.web.dto;

import lombok.*;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/23/16
 */
@Getter
@Setter
public class SimplePaginationFilter {
    private Integer currentPage;
    private Integer entriesOnPage;
    private String sort;
    private String search;
}
