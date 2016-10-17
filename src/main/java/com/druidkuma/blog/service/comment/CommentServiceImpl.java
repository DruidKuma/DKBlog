package com.druidkuma.blog.service.comment;

import com.druidkuma.blog.dao.comment.CommentRepository;
import com.druidkuma.blog.dao.comment.specification.CommentSearchCriteria;
import com.druidkuma.blog.dao.comment.specification.CommentSpecification;
import com.druidkuma.blog.domain.comment.Comment;
import com.druidkuma.blog.web.dto.comment.BlogCommentInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<BlogCommentInfoDto> getPageOfComments(Pageable pageRequest, String ipFilter, Long postFilter, String typeFilter) {
        Page<Comment> comments = commentRepository.findAll(
                new CommentSpecification(
                        new CommentSearchCriteria(ipFilter, typeFilter, postFilter)),
                pageRequest);
        return null;
    }
}
