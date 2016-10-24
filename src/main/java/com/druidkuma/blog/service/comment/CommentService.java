package com.druidkuma.blog.service.comment;

import com.druidkuma.blog.domain.comment.Comment;
import com.druidkuma.blog.domain.comment.CommentType;
import com.druidkuma.blog.web.dto.comment.BlogCommentInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/20/16
 */
public interface CommentService {
    List<Comment> getAllCommentsForBlogPost(Long blogPostId);
    Comment saveComment(Comment comment);

    Comment getOne(Long commentId);

    Page<BlogCommentInfoDto> getPageOfComments(Pageable pageRequest, String ipFilter, Long postFilter, String typeFilter);

    void updateTypeForComments(List<Long> commentIds, CommentType type);

    void updateComment(BlogCommentInfoDto commentDto);
}
