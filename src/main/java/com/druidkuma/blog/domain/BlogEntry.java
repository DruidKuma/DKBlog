package com.druidkuma.blog.domain;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Set;

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
@Table(name = "blog_entry")
public class BlogEntry {

    @Id
    @Column(name = "be_id")
    @SequenceGenerator(name = "blog_entry_be_id_seq", sequenceName = "blog_entry_be_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blog_entry_be_id_seq")
    private Long id;

    @Column(name = "be_author")
    private String author;

    @Column(name = "be_permalink", nullable = false)
    private String permalink;

    @Column(name = "be_creation_date", nullable = false)
    private Instant creationDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "be_content_id")
    private Content content;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cm_blog_entry_id")
    private List<Comment> comments;

    @Column(name = "be_comments_enabled")
    private Boolean commentsEnabled;
    @Column(name = "be_is_published")
    private Boolean isPublished;

    @Column(name = "be_num_views")
    private Long numViews;
    @Column(name = "be_num_comments")
    private Long numComments;

    @Transient
    private Set<Tag> tags;
    @Transient
    private Set<Category> categories;
}
