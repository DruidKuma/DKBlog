package com.druidkuma.blog.service.comment;

import com.druidkuma.blog.domain.Comment;

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
}
