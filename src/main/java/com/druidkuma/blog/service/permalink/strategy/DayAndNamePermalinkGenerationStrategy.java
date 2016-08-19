package com.druidkuma.blog.service.permalink.strategy;

import com.druidkuma.blog.service.permalink.PermalinkForm;
import com.druidkuma.blog.util.NormalizationUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/19/16
 */
public class DayAndNamePermalinkGenerationStrategy implements PermalinkGenerationStrategy {
    @Override
    public String generate(PermalinkForm form) {
        LocalDateTime creationDate = LocalDateTime.ofInstant(form.getDate(), ZoneId.of("UTC"));
        return String.format("%d/%d/%d/%s",
                creationDate.get(ChronoField.YEAR),
                creationDate.get(ChronoField.MONTH_OF_YEAR),
                creationDate.get(ChronoField.DAY_OF_MONTH),
                NormalizationUtil.normalizeUrlNameKey(form.getTitle()));
    }
}
