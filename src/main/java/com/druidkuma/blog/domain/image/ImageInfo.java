package com.druidkuma.blog.domain.image;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/4/16
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "image_info")
public class ImageInfo {

    @Id
    @Column(name = "ii_id")
    @SequenceGenerator(name = "image_info_ii_id_seq", sequenceName = "image_info_ii_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_info_ii_id_seq")
    private Long id;

    @Column(name = "ii_image_file_name")
    private String imageFileName;

    @Column(name = "ii_created_at")
    private Instant createdAt;
}
