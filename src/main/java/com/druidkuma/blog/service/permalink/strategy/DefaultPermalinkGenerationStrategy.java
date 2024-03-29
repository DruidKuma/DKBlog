package com.druidkuma.blog.service.permalink.strategy;

import com.druidkuma.blog.service.permalink.PermalinkForm;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/19/16
 */
public class DefaultPermalinkGenerationStrategy implements PermalinkGenerationStrategy {
    @Override
    public String generate(PermalinkForm form) {
        return String.format("?p=%s", form.getId() == null ? "unknown" : String.valueOf(form.getId()));
    }
}
