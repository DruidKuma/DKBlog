package com.druidkuma.blog.domain.category;

import com.druidkuma.blog.domain.BlogEntry;
import com.druidkuma.blog.domain.country.Country;
import com.druidkuma.blog.domain.i18n.Translation;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
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
@Table(name = "category")
public class Category implements Serializable {

    @Id
    @Column(name = "ct_id")
    @SequenceGenerator(name = "category_ct_id_seq", sequenceName = "category_ct_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_ct_id_seq")
    private Long id;

    @Column(name = "ct_name_key")
    private String nameKey;

    @Column(name = "ct_hex_color")
    private String hexColor;

    @Column(name = "ct_text_color")
    private String textColor;

    @Column(name = "ct_last_modified")
    private Instant lastModified;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "tr_key", referencedColumnName = "ct_name_key")
    @Where(clause = "(tr_group_id = resolve_translation_group_by_full_name('components.category'))")
    private List<Translation> translations;

    @ManyToMany
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "blog_entry_2_category",
            joinColumns = @JoinColumn(name = "be2c_category_id"),
            inverseJoinColumns = @JoinColumn(name = "be2c_blog_entry_id"))
    private List<BlogEntry> blogEntries;

    @ManyToMany
    @JoinTable(name = "category_2_country",
            joinColumns = @JoinColumn(name = "c2c_category_id"),
            inverseJoinColumns = @JoinColumn(name = "c2c_country_id"))
    private Set<Country> countries;
}
