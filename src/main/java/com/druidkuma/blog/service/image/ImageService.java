package com.druidkuma.blog.service.image;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/4/16
 */
public interface ImageService {

    Path loadNewImage(MultipartFile file);
}
