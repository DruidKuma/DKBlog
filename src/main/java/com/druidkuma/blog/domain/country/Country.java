package com.druidkuma.blog.domain.country;

import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.OrderBy;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/22/16
 */
@Entity
@Table(name = "country")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Country {

    @Id
    @SequenceGenerator(name = "country_c_id_seq", sequenceName = "country_c_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "country_c_id_seq")
    @Column(name = "c_id")
    private Long id;

    @Column(name = "c_name")
    private String name;

    @Column(name = "c_iso_2_alpha")
    private String isoAlpha2Code;

    @Column(name = "c_iso_3_alpha")
    private String isoAlpha3CodeCode;

    @Column(name = "c_iso_numeric")
    private String isoNumeric;

    @Column(name = "c_enabled")
    private Boolean isEnabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "country_2_language",
            joinColumns = @JoinColumn(name = "c2l_country_id"),
            inverseJoinColumns = @JoinColumn(name = "c2l_language_id"))
    private List<Language> languages;

    @ManyToOne
    @JoinFormula(value = "(SELECT c2l.c2l_language_id FROM country_2_language c2l WHERE c2l.c2l_country_id = c_id AND c2l.c2l_is_default)")
    private Language defaultLanguage;
}
