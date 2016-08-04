package com.druidkuma.blog.service.image;

import com.druidkuma.blog.dao.image.ImageInfoRepository;
import com.druidkuma.blog.domain.image.ImageInfo;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.List;
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

    private ImageInfoRepository imageInfoRepository;

    @Autowired
    public ImageServiceImpl(ImageInfoRepository imageInfoRepository) {
        this.imageInfoRepository = imageInfoRepository;
    }

    @Override
    @SneakyThrows
    public void loadNewImage(MultipartFile file, String basePath) {
        String fileName = FilenameUtils.getBaseName(file.getOriginalFilename().replaceAll(" ", "-"));
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        new File(BASE_PATH).mkdirs();
        File fullPath = new File(String.format("%s/%s-%s.%s",
                BASE_PATH,
                fileName,
                UUID.randomUUID(),
                extension).replace("/", System.getProperty("file.separator")));
        Path path = Files.write(fullPath.toPath(), file.getBytes(), StandardOpenOption.CREATE);

        imageInfoRepository.saveAndFlush(ImageInfo.builder()
                .imagePath(basePath + "/" + path.getFileName())
                .createdAt(Instant.now()).build());
    }

    @Override
    @SneakyThrows
    public byte[] getImageByFileName(String fileName) {
        return IOUtils.toByteArray(new FileInputStream(BASE_PATH + "/" + fileName));
    }

    @Override
    public Page<List<String>> getPageOfImageUrls(PageRequest pageRequest) {
        return imageInfoRepository.getPageOfImageUrls(pageRequest);
    }
}
