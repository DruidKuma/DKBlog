package com.druidkuma.blog.web.transformer;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/5/16
 */
public interface DtoTransformer<E, D> {
    E transformFromDto(D dto);
    D tranformToDto(E entity);
}
