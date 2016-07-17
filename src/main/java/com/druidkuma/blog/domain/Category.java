package com.druidkuma.blog.domain;

import lombok.*;

import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/13/16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    private Integer id;
    private String name;
    private Category parent;
    private List<Category> subCategories;
    private List<BlogEntry> blogEntries;
}
