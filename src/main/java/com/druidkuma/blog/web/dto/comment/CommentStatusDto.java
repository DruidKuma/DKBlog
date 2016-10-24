package com.druidkuma.blog.web.dto.comment;

import com.druidkuma.blog.domain.comment.CommentType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 10/21/16
 */
@Getter
@Setter
public class CommentStatusDto {
    private List<Long> commentIds;
    private CommentType status;
}
