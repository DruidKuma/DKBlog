package com.druidkuma.blog.dao.image;

import com.druidkuma.blog.domain.image.ImageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/4/16
 */
@Repository
public interface ImageInfoRepository extends JpaRepository<ImageInfo, Long> {
}
