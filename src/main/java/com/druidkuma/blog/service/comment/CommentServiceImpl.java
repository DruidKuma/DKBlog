package com.druidkuma.blog.service.comment;

import com.druidkuma.blog.dao.CommentRepository;
import com.druidkuma.blog.domain.Comment;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/20/16
 */
@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> getAllCommentsForBlogPost(Long blogPostId) {
        return commentRepository.findByBlogEntryId(blogPostId);
    }
}
