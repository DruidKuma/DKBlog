package com.druidkuma.blog.domain.country;

import lombok.*;

import javax.persistence.*;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/22/16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "language")
public class Language {

    @Id
    @SequenceGenerator(name = "language_l_id_seq", sequenceName = "language_l_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "language_l_id_seq")
    @Column(name = "l_id")
    private Long id;

    @Column(name = "l_iso_code")
    private String isoCode;

    @Column(name = "l_name")
    private String name;

    @Column(name = "l_native_name")
    private String nativeName;

    @Override
    public int hashCode() {
        return this.isoCode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return !(isoCode != null ? !isoCode.equals(language.isoCode) : language.isoCode != null);
    }
}
