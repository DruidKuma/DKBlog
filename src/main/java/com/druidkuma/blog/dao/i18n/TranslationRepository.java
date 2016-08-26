package com.druidkuma.blog.dao.i18n;

import com.druidkuma.blog.domain.i18n.Translation;
import com.druidkuma.blog.domain.i18n.TranslationGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {
    List<Translation> findByTranslationGroupAndLanguageIsoCode(TranslationGroup translationGroup, String languageIsoCode);
    List<Translation> findByTranslationGroupAndKey(TranslationGroup translationGroup, String key);
    List<Translation> findByTranslationGroup(TranslationGroup translationGroup);
    List<Translation> findByLanguageIsoCode(String languageIsoCode);
    Translation findByTranslationGroupAndKeyAndLanguageIsoCode(TranslationGroup translationGroup, String key, String languageIsoCode);

    @Query(value = "SELECT DISTINCT tr.key FROM Translation tr WHERE tr.translationGroup = :translationGroup")
    List<String> getAllTranslationKeysForGroup(@Param("translationGroup") TranslationGroup translationGroup);

    @Query(value = "SELECT DISTINCT tr.key FROM Translation tr WHERE tr.translationGroup.id = :groupId")
    Page<String> getTranslationKeysForGroup(@Param("groupId") Long groupId, Pageable pageable);

    @Query(value = "SELECT DISTINCT tr.key FROM Translation tr WHERE tr.translationGroup.id = :groupId AND lower(tr.key) LIKE concat('%', lower(:search) , '%')")
    Page<String> getTranslationKeysForGroupWithSearch(@Param("groupId") Long groupId, Pageable pageable, @Param("search") String search);
}
