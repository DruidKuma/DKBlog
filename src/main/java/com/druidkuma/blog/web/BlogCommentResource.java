package com.druidkuma.blog.web;

import com.druidkuma.blog.domain.comment.Comment;
import com.druidkuma.blog.domain.comment.CommentType;
import com.druidkuma.blog.service.blogentry.BlogEntryService;
import com.druidkuma.blog.service.comment.CommentService;
import com.druidkuma.blog.web.dto.comment.BlogCommentDto;
import com.druidkuma.blog.web.dto.comment.BlogCommentInfoDto;
import com.druidkuma.blog.web.dto.comment.CommentStatusDto;
import com.druidkuma.blog.web.dto.filter.CommentFilter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
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
    private BlogEntryService blogEntryService;

    @Autowired
    public BlogCommentResource(CommentService commentService, BlogEntryService blogEntryService) {
        this.commentService = commentService;
        this.blogEntryService = blogEntryService;
    }

    @RequestMapping(value = "/{id}")
    public List<BlogCommentDto> getCommentForBlogPost(@PathVariable("id") Long id) {
        return commentService.getAllCommentsForBlogPost(id)
                .stream()
                .map(this::buildCommentDto)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    public BlogCommentDto saveComment(@RequestBody BlogCommentDto comment, HttpServletRequest request) {
        return buildCommentDto(commentService.saveComment(buildComment(comment, request)));
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public Page<BlogCommentInfoDto> getPageOfBlogComments(@RequestBody CommentFilter commentFilter) {
        Pageable pageRequest = commentFilter.toPageRequest("creationDate DESC");
        return commentService.getPageOfComments(
                pageRequest,
                commentFilter.getIpFilter(),
                commentFilter.getPostFilter(),
                commentFilter.getTypeFilter());
    }

    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public void changeCommentStatus(@RequestBody CommentStatusDto commentStatusDto) {
        commentService.updateTypeForComments(commentStatusDto.getCommentIds(), commentStatusDto.getStatus());
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void updateComment(@RequestBody BlogCommentInfoDto commentDto) {
        commentService.updateComment(commentDto);
    }

    private BlogCommentDto buildCommentDto(Comment comment) {
        return BlogCommentDto.builder()
                .author(comment.getAuthor())
                .body(comment.getBody())
                .creationDate(comment.getCreationDate())
                .id(comment.getId())
                .children(comment.getNestedComments() != null ? comment.getNestedComments().stream().map(this::buildCommentDto).collect(Collectors.toList()) : Lists.newArrayList())
                .build();
    }

    private Comment buildComment(BlogCommentDto commentDto, HttpServletRequest request) {
        return Comment.builder()
                .type(CommentType.APPROVED)
                .author(commentDto.getAuthor())
                .authorUserAgent(request.getHeader("user-agent"))
                .authorIp(request.getRemoteAddr())
                .body(commentDto.getBody())
                .email("admin@blog.com")
                .creationDate(Instant.now())
                .blogEntry(blogEntryService.getOne(commentDto.getBlogPostId()))
                .id(commentDto.getId())
                .parent(commentDto.getParentId() != null ? commentService.getOne(commentDto.getParentId()) : null)
                .build();
    }
}
