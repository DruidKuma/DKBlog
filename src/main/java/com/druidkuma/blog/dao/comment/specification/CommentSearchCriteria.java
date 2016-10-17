package com.druidkuma.blog.dao.comment.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 10/17/16
 */
@Getter
@AllArgsConstructor
public class CommentSearchCriteria {
    private String ipFilter;
    private String typeFilter;
    private Long postFilter;
}
