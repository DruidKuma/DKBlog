package com.druidkuma.blog.service.permalink.strategy;

import com.druidkuma.blog.service.permalink.PermalinkForm;
import com.druidkuma.blog.util.NormalizationUtil;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/19/16
 */
public class PostNamePermalinkGenerationStrategy implements PermalinkGenerationStrategy {
    @Override
    public String generate(PermalinkForm form) {
        return NormalizationUtil.normalizeUrlNameKey(form.getTitle());
    }
}
