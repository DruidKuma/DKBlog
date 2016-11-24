package com.druidkuma.blog.domain.entry;

import lombok.*;

import javax.persistence.*;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 11/24/16
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "seo_settings")
public class SeoSettings {

    @Id
    @Column(name = "ss_id")
    @SequenceGenerator(name = "seo_settings_ss_id_seq", sequenceName = "seo_settings_ss_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seo_settings_ss_id_seq")
    private Long id;

    @Column(name = "ss_title")
    private String title;

    @Column(name = "ss_description")
    private String description;

    @Column(name = "ss_keywords")
    private String keywords;

    @Column(name = "ss_meta_robots_nofollow")
    private Boolean metaRobotsNoFollow;

    @Column(name = "ss_meta_robots_noindex")
    private Boolean metaRobotsNoIndex;

    @Column(name = "ss_og_description")
    private String ogDescription;

    @Column(name = "ss_og_image")
    private String ogImage;

    @Column(name = "ss_og_title")
    private String ogTitle;

    @Column(name = "ss_og_type")
    private String ogType;
}
