package com.druidkuma.blog.web;

import com.druidkuma.blog.domain.image.ImageInfo;
import com.druidkuma.blog.service.image.ImageService;
import com.druidkuma.blog.web.dto.ImageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/3/16
 */
@RestController
@RequestMapping(value = "/api/blog/image")
public class ImageResource {

    public static final String BASE_IMAGE_URL = "http://localhost:8081/api/blog/image";

    private ImageService imageService;

    @Autowired
    public ImageResource(ImageService imageService) {
        this.imageService = imageService;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void upload(@RequestParam(value = "file") MultipartFile file) {
        imageService.loadNewImage(file, BASE_IMAGE_URL);
    }

    @RequestMapping(value = "/render/{fileName:.+}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getImage(@PathVariable("fileName") String fileName) {
        return createImageHttpEntity(imageService.getImageByFileName(fileName));
    }

    @RequestMapping(value = "/thumb/{fileName:.+}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getThumbImage(@PathVariable("fileName") String fileName) {
        return createImageHttpEntity(imageService.getThumbnailByFileName(fileName));
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<ImageDto> getPageOfImageThumbnailUrls(@RequestParam("page") Integer page, @RequestParam("numOnPage") Integer numOnPage) {
        Pageable pageRequest = new PageRequest(page, numOnPage, Sort.Direction.DESC, "createdAt");
        Page<ImageInfo> imageInfos = imageService.getPageOfImages(pageRequest);
        return new PageImpl<>(
                imageInfos.getContent().stream()
                        .map(imageInfo -> ImageDto.builder()
                                .id(imageInfo.getId())
                                .createdAt(imageInfo.getCreatedAt())
                                .fullImgSrc(BASE_IMAGE_URL + "/render/" + imageInfo.getImageFileName())
                                .thumbImgSrc(BASE_IMAGE_URL + "/thumb/" + imageInfo.getImageFileName())
                                .build())
                        .collect(Collectors.toList()),
                pageRequest,
                imageInfos.getTotalElements());
    }

    private HttpEntity<byte[]> createImageHttpEntity(byte[] image) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(image, headers, HttpStatus.CREATED);
    }
}
