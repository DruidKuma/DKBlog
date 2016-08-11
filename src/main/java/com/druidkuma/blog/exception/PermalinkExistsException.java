package com.druidkuma.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author DruidKuma
 * @version 1.0.0
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class PermalinkExistsException extends RuntimeException {
}
