package com.druidkuma.blog.dao.category;

import com.druidkuma.blog.domain.category.Category;
import org.springframework.data.annotation.Transient;
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
 * @since 7/28/16
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    Category findByNameKey(String categoryName);

    @Query(value = "SELECT ct.* " +
            "FROM category ct\n" +
            "  JOIN category_2_country c2c ON ct.ct_id = c2c.c2c_category_id " +
            "  JOIN country c ON c2c.c2c_country_id = c.c_id " +
            "WHERE c.c_iso_2_alpha = :countryIso " +
            "ORDER BY sort_order", nativeQuery = true)
    List<Category> findCategoriesForCountryOrdered(@Param("countryIso") String countryIso);

    @Modifying
    @Transactional
    @Query(value = "UPDATE category_2_country SET sort_order = :sortOrder " +
            "WHERE c2c_category_id = :categoryId AND c2c_country_id = " +
            "(SELECT c_id FROM country WHERE c_iso_2_alpha = :countryIso)", nativeQuery = true)
    Integer updateCategorySortOrderForCountry(@Param("categoryId") Long categoryId,
                                              @Param("sortOrder") Integer sortOrder,
                                              @Param("countryIso") String countryIso);
}
