package com.druidkuma.blog.dao.category.specification;

import com.druidkuma.blog.domain.Category;
import com.druidkuma.blog.domain.country.Country;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 7/28/16
 */
@AllArgsConstructor
public class CategorySpecification implements Specification<Category> {

    List<Country> matchCountries;

    @Override
    public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {

        List<Predicate> predicates = Lists.newArrayList();
        for (Country country : matchCountries) {
            predicates.add(builder.isMember(country, root.get("countries")));
        }

        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
    }

}
