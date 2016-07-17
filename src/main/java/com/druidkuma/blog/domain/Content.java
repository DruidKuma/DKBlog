package com.druidkuma.blog.domain;

import lombok.*;

import javax.persistence.*;

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
@Entity
@Table(name = "content")
public class Content {

    @Id
    @Column(name = "c_id")
    @SequenceGenerator(name = "content_c_id_seq", sequenceName = "content_c_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_c_id_seq")
    private Integer id;

    @Column(name = "c_image_url")
    private String imageUrl;

    @Column(name = "c_title")
    private String title;

    @Column(name = "c_contents")
    private String contents;
}
