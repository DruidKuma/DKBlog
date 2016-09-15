package com.druidkuma.blog.web.dto.filter;

import lombok.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    public Pageable toPageRequest(String defaultSorting) {
        Integer page = this.getCurrentPage() - 1;
        Integer pageSize = this.getEntriesOnPage();

        String[] sort = StringUtils.isBlank(this.getSort()) || this.getSort().split(" ").length != 2
                ? defaultSorting.split(" ")
                : this.getSort().split(" ");

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        return new PageRequest(page, pageSize, direction, sort[0]);
    }
}
