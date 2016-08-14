package com.druidkuma.blog.domain;

import com.druidkuma.blog.domain.category.Category;
import com.druidkuma.blog.domain.country.Country;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "blogEntry")
    @Where(clause = "cm_comment_parent_id IS NULL")
    private List<Comment> comments;

    @Column(name = "be_comments_enabled")
    private Boolean commentsEnabled;
    @Column(name = "be_is_published")
    private Boolean isPublished;

    @Column(name = "be_num_views")
    private Long numViews;
    @Column(name = "be_num_comments")
    private Long numComments;

    @ManyToMany
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "blog_entry_2_country",
            joinColumns = @JoinColumn(name = "be2c_blog_entry_id"),
            inverseJoinColumns = @JoinColumn(name = "be2c_country_id"))
    private List<Country> countries;

    @ManyToMany
    @JoinTable(name = "blog_entry_2_category",
            joinColumns = @JoinColumn(name = "be2c_blog_entry_id"),
            inverseJoinColumns = @JoinColumn(name = "be2c_category_id"))
    private List<Category> categories;

    @Transient
    private Set<Tag> tags;
}
