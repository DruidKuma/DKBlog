package com.druidkuma.blog.service.image;

import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/4/16
 */
@Service
public class ImageServiceImpl implements ImageService {

    public static final String BASE_PATH = "/data/dobby/images";

    @Override
    @SneakyThrows
    public Path loadNewImage(MultipartFile file) {
        String fileName = FilenameUtils.getBaseName(file.getOriginalFilename().replaceAll(" ", "-"));
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        new File(BASE_PATH).mkdirs();
        File fullPath = new File(String.format("%s/%s-%s.%s",
                BASE_PATH,
                fileName,
                UUID.randomUUID(),
                extension).replace("/", System.getProperty("file.separator")));
        return Files.write(fullPath.toPath(), file.getBytes(), StandardOpenOption.CREATE);
    }
}
