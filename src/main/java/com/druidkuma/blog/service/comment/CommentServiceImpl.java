package com.druidkuma.blog.service.comment;

import com.druidkuma.blog.dao.comment.CommentRepository;
import com.druidkuma.blog.dao.comment.specification.CommentSearchCriteria;
import com.druidkuma.blog.dao.comment.specification.CommentSpecification;
import com.druidkuma.blog.domain.comment.Comment;
import com.druidkuma.blog.domain.comment.CommentType;
import com.druidkuma.blog.web.dto.comment.BlogCommentInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

        return new PageImpl<>(
                comments.getContent().stream()
                        .map(comment -> BlogCommentInfoDto
                                .builder()
                                .id(comment.getId())
                                .author(comment.getAuthor())
                                .email(comment.getEmail())
                                .ipAddress(comment.getAuthorIp())
                                .date(comment.getCreationDate())
                                .text(comment.getBody())
                                .type(comment.getType().name())
                                .postId(comment.getBlogEntry().getId())
                                .build())
                        .collect(Collectors.toList()), pageRequest, comments.getTotalElements());
    }

    @Override
    public void updateTypeForComments(List<Long> commentIds, CommentType type) {
        commentRepository.updateTypeForComments(commentIds, type);
    }

    @Override
    public void updateComment(BlogCommentInfoDto commentDto) {
        Comment comment = commentRepository.findOne(commentDto.getId());
        comment.setAuthor(commentDto.getAuthor());
        comment.setBody(commentDto.getText());
        comment.setType(CommentType.valueOf(commentDto.getType()));
        commentRepository.saveAndFlush(comment);
    }
}
