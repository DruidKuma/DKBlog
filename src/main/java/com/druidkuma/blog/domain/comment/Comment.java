package com.druidkuma.blog.domain.comment;

import com.druidkuma.blog.domain.entry.BlogEntry;
import com.google.common.collect.Lists;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.Instant;
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
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @Column(name = "cm_id")
    @SequenceGenerator(name = "comments_cm_id_seq", sequenceName = "comments_cm_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_cm_id_seq")
    private Long id;

    @Column(name = "cm_body")
    private String body;

    @Column(name = "cm_author")
    private String author;

    @Column(name = "cm_author_email")
    private String email;

    @Column(name = "cm_author_ip")
    private String authorIp;

    @Column(name = "cm_author_user_agent")
    private String authorUserAgent;

    @Column(name = "cm_approved")
    private Boolean isApproved;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cm_comment_parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<Comment> nestedComments;

    @Column(name = "cm_creation_date")
    private Instant creationDate;

    @Column(name = "cm_type")
    @Enumerated(value = EnumType.STRING)
    private CommentType type;

    @Column(name = "cm_rate")
    private Integer rate;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "cm_blog_entry_id")
    private BlogEntry blogEntry;

    public void addParent(Comment comment) {
        if(comment == null) return;
        this.parent = comment;
        if(comment.getNestedComments() == null) {
            comment.setNestedComments(Lists.newArrayList());
        }
        comment.getNestedComments().add(this);
    }
}
