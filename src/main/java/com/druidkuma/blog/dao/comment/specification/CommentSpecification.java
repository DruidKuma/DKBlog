package com.druidkuma.blog.dao.comment.specification;

import com.druidkuma.blog.domain.entry.BlogEntry;
import com.druidkuma.blog.domain.comment.Comment;
import com.druidkuma.blog.domain.comment.CommentType;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 10/17/16
 */
@AllArgsConstructor
public class CommentSpecification implements Specification<Comment> {

    private CommentSearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = buildPredicates(root, query, cb);
        return predicates.size() > 0
                ? cb.and(predicates.toArray(new Predicate[predicates.size()]))
                : null;
    }

    private List<Predicate> buildPredicates(Root<Comment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {

        List<Predicate> result = Lists.newArrayList();

        if(StringUtils.isNotBlank(criteria.getIpFilter())) {
            result.add(builder.like(builder.lower(root.<String>get("authorIp")), "%" + criteria.getIpFilter().toLowerCase() + "%"));
        }
        String typeFilter = criteria.getTypeFilter();
        if(StringUtils.isNotBlank(typeFilter)) {
            CommentType commentType = CommentType.valueOf(typeFilter);
            result.add(builder.equal(root.<CommentType>get("type"), commentType));
        }
        if(criteria.getPostFilter() != null) {
            result.add(builder.equal(root.<BlogEntry>get("blogEntry").<Long>get("id"), criteria.getPostFilter()));
        }

        return result;
    }

}
