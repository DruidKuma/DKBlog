package com.druidkuma.blog.exception;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/19/16
 */
public class PermalinkStrategyNotFoundException extends RuntimeException {
    public PermalinkStrategyNotFoundException(String message) {
        super("Unknown Permalink generation strategy : " + message);
    }
}
