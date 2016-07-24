package com.druidkuma.blog.domain.i18n;

import com.druidkuma.blog.domain.country.Language;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "translations")
public class Translation {

    @Id
    @Column(name = "tr_id")
    @SequenceGenerator(name = "translations_tr_id_seq", sequenceName = "translations_tr_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "translations_tr_id_seq")
    private Long id;

    @Column(name = "tr_key")
    private String key;

    @Column(name = "tr_value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "tr_language_id")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "tr_group_id")
    private TranslationGroup translationGroup;

    @Column(name = "tr_last_modified")
    private Instant lastModified;
}
