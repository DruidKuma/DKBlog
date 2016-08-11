package com.druidkuma.blog.dao.blogEntry.specification;

import com.druidkuma.blog.domain.BlogEntry;
import com.druidkuma.blog.domain.Content;
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
 * @since 7/20/16
 */
@AllArgsConstructor
public class BlogEntrySpecification implements Specification<BlogEntry> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<BlogEntry> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        List<Predicate> predicates = buildPredicates(root, criteriaQuery, builder);
        return predicates.size() > 1
                ? builder.and(predicates.toArray(new Predicate[predicates.size()]))
                : predicates.get(0);
    }

    private List<Predicate> buildPredicates(Root<BlogEntry> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {

        List<Predicate> result = Lists.newArrayList(builder.isMember(criteria.getCountry(), root.get("countries")));

        if(StringUtils.isNotBlank(criteria.getFilterPublished())) {
            Boolean filterPublished = Boolean.valueOf(criteria.getFilterPublished());
            result.add(builder.equal(root.<Boolean>get("isPublished"), filterPublished));
        }
        if(StringUtils.isNotBlank(criteria.getSearch())) {
            result.add(builder.like(builder.lower(root.<Content>get("content").<String>get("title")), "%" + criteria.getSearch().toLowerCase() + "%"));
        }
        if(criteria.getCategory() != null) {
            result.add(builder.isMember(criteria.getCategory(), root.get("categories")));
        }

        return result;
    }
}
