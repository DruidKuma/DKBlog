package com.druidkuma.blog.exception;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
public class UnknownPropertySourceException extends RuntimeException {
    public UnknownPropertySourceException(String message) {
        super("Unknown Property Source: " + message);
    }
}
