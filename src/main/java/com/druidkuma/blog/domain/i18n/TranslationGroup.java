package com.druidkuma.blog.domain.i18n;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "translation_groups")
@JsonIgnoreProperties(value = "childGroups")
public class TranslationGroup {

    @Id
    @Column(name = "tg_id")
    @SequenceGenerator(name = "translation_groups_tg_id_seq", sequenceName = "translation_groups_tg_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "translation_groups_tg_id_seq")
    private Long id;

    @Column(name = "tg_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "tg_parent_group_id")
    private TranslationGroup parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    private List<TranslationGroup> childGroups;
}
