package com.druidkuma.blog.service.comment;

import com.druidkuma.blog.dao.comment.CommentRepository;
import com.druidkuma.blog.domain.comment.Comment;
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
        return commentRepository.findByBlogEntryIdAndParentIsNull(blogPostId);
    }

    @Override
    public Comment saveComment(Comment comment) {
        comment.addParent(comment.getParent());
        return commentRepository.saveAndFlush(comment);
    }

    @Override
    public Comment getOne(Long commentId) {
        return commentRepository.findOne(commentId);
    }
}
