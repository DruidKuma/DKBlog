package com.druidkuma.blog.dao.comment;

import com.druidkuma.blog.domain.comment.Comment;
import com.druidkuma.blog.domain.comment.CommentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/16/16
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    List<Comment> findByBlogEntryIdAndParentIsNull(Long blogEntryId);

    @Query("UPDATE Comment c SET c.type = :newType WHERE c.id IN :commentIds")
    @Modifying
    @Transactional
    void updateTypeForComments(@Param("commentIds") List<Long> commentIds,
                               @Param("newType") CommentType type);
}
