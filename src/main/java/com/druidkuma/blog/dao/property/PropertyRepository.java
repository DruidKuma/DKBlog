package com.druidkuma.blog.dao.property;

import com.druidkuma.blog.domain.property.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/15/16
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
}
