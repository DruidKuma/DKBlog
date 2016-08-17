package com.druidkuma.blog.domain.property;

import com.druidkuma.blog.domain.country.Country;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.Instant;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/15/16
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "property")
public class Property {

    @Id
    @SequenceGenerator(name = "property_pr_id_seq", sequenceName = "property_pr_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "property_pr_id_seq")
    @Column(name = "pr_id")
    private Long id;

    @Column(name = "pr_key")
    private String key;

    @ManyToOne
    @JoinColumn(name = "pr_country_id")
    @Fetch(FetchMode.SELECT)
    private Country country;

    @Column(name = "pr_value")
    private String value;

    @Column(name = "pr_last_modified")
    private Instant lastModified;
}
