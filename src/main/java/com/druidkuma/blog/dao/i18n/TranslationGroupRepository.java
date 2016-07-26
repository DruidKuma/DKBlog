package com.druidkuma.blog.dao.i18n;

import com.druidkuma.blog.domain.i18n.TranslationGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
@Repository
public interface TranslationGroupRepository extends JpaRepository<TranslationGroup, Long> {
    TranslationGroup findByName(String name);
    List<TranslationGroup> findAllByParentIsNull();
}
