package com.druidkuma.blog.exception;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/25/16
 */
public class TranslationGroupNotExistsException extends RuntimeException {
    public TranslationGroupNotExistsException(String group) {
        super("Translation group does not exist: " + group);
    }
}
