package com.druidkuma.blog.service.image;

import com.druidkuma.blog.dao.image.ImageInfoRepository;
import com.druidkuma.blog.domain.image.ImageInfo;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
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

    public static final String BASE_PATH = "/data/blog/images";
    public static final String BASE_THUMB_PATH = "/data/blog/images/thumbs";
    public static final Integer THUMB_SIZE = 80;


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
        UUID imageHash = UUID.randomUUID();

        //save original image
        new File(BASE_PATH).mkdirs();

        File fullPath = new File(createFilePath(BASE_PATH, fileName, extension, imageHash));
        Path path = Files.write(fullPath.toPath(), file.getBytes(), StandardOpenOption.CREATE);

        //resize image and create thumbnail
        new File(BASE_THUMB_PATH).mkdirs();

        File thumbPath = new File(createFilePath(BASE_THUMB_PATH, fileName, extension, imageHash));
        saveImage(ImageIO.read(file.getInputStream()), extension, thumbPath);

        //save image to DB
        imageInfoRepository.saveAndFlush(ImageInfo.builder()
                .imageFileName(path.getFileName().toString())
                .createdAt(Instant.now()).build());
    }

    @SneakyThrows
    public Path saveImage(BufferedImage originalImage, String extension, File fullPath) {
        BufferedImage resizeImageJpg = resizeImage(originalImage);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizeImageJpg, extension, baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return Files.write(fullPath.toPath(), imageInByte, StandardOpenOption.CREATE);
    }

    @SneakyThrows
    private BufferedImage resizeImage(BufferedImage originalImage) {
        return Scalr.resize(originalImage, Scalr.Mode.FIT_TO_HEIGHT, THUMB_SIZE);
    }

    public String createFilePath(String basePath, String fileName, String extension, UUID randomHash) {
        return String.format("%s/%s-%s.%s",
                basePath,
                fileName,
                randomHash,
                extension).replace("/", System.getProperty("file.separator"));
    }

    @Override
    @SneakyThrows
    public byte[] getImageByFileName(String fileName) {
        return IOUtils.toByteArray(new FileInputStream(BASE_PATH + "/" + fileName));
    }

    @Override
    public Page<ImageInfo> getPageOfImages(Pageable pageRequest) {
        return imageInfoRepository.findAll(pageRequest);
    }

    @Override
    @SneakyThrows
    public byte[] getThumbnailByFileName(String fileName) {
        return IOUtils.toByteArray(new FileInputStream(BASE_THUMB_PATH + "/" + fileName));
    }
}
