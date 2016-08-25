package com.druidkuma.blog.exception;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/25/16
 */
public class TranslationGroupExistsException extends RuntimeException {
    public TranslationGroupExistsException(String group, String key) {
        super(String.format("Such translation group already exists: %s.%s", group, key));
    }
}
