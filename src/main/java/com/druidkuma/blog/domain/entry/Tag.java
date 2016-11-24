package com.druidkuma.blog.domain.entry;

import lombok.*;

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
public class Tag {
    private Integer id;
    private String name;
    private int rank;
}
