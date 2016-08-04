package com.druidkuma.blog.service.image;

import com.druidkuma.blog.domain.image.ImageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/4/16
 */
public interface ImageService {

    void loadNewImage(MultipartFile file, String basePath);

    byte[] getImageByFileName(String fileName);

    Page<ImageInfo> getPageOfImages(Pageable pageRequest);

    byte[] getThumbnailByFileName(String fileName);
}
