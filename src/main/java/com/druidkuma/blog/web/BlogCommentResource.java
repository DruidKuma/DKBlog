package com.druidkuma.blog.web;

import com.druidkuma.blog.domain.Comment;
import com.druidkuma.blog.service.comment.CommentService;
import com.druidkuma.blog.web.dto.BlogCommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/20/16
 */
@RestController
@RequestMapping(value = "/api/blog/comment")
public class BlogCommentResource {

    private CommentService commentService;

    @Autowired
    public BlogCommentResource(CommentService commentService) {
        this.commentService = commentService;
    }

    @RequestMapping(value = "/{id}")
    public List<BlogCommentDto> getCommentForBlogPost(@PathVariable("id") Long id) {
        return commentService.getAllCommentsForBlogPost(id)
                .stream()
                .map(this::buildCommentDto)
                .collect(Collectors.toList());
    }

    private BlogCommentDto buildCommentDto(Comment comment) {
        return BlogCommentDto.builder()
                .author(comment.getAuthor())
                .body(comment.getBody())
                .creationDate(comment.getCreationDate())
                .id(comment.getId())
                .children(comment.getNestedComments().stream().map(this::buildCommentDto).collect(Collectors.toList()))
                .build();
    }
}
