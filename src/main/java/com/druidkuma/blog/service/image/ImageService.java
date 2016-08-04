package com.druidkuma.blog.service.image;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    Page<List<String>> getPageOfImageUrls(PageRequest pageRequest);
}
