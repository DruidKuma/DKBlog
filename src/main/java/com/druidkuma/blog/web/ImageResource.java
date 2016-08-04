package com.druidkuma.blog.web;

import com.druidkuma.blog.service.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    public static final String BASE_IMAGE_URL = "http://localhost:8081/api/blog/image/render";

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

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        byte[] imageBytes = imageService.getImageByFileName(fileName);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<List<String>> getPageOfImageUrls(@RequestParam("page") Integer page, @RequestParam("numOnPage") Integer numOnPage) {
        return imageService.getPageOfImageUrls(new PageRequest(page, numOnPage, Sort.Direction.DESC, "createdAt"));
    }
}
