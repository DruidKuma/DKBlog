package com.druidkuma.blog.dao.blogEntry.specification;

import com.druidkuma.blog.domain.category.Category;
import com.druidkuma.blog.domain.country.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/20/16
 */
@Getter
@AllArgsConstructor
public class SearchCriteria {
    private String search;
    private String filterPublished;
    private Category category;
    private Country country;
}
