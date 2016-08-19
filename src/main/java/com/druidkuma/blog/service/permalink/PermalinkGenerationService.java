package com.druidkuma.blog.service.permalink;

import com.druidkuma.blog.exception.PermalinkStrategyNotFoundException;
import com.druidkuma.blog.service.permalink.strategy.*;
import com.druidkuma.blog.service.property.CommonKeys;
import com.druidkuma.blog.service.property.PropertyService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Map;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 8/19/16
 */
@Service
public class PermalinkGenerationService {

    @Autowired
    private PropertyService propertyService;

    private Map<String, PermalinkGenerationStrategy> strategies = Maps.newHashMap();

    @PostConstruct
    public void fillStrategies() {
        strategies.put("default", new DefaultPermalinkGenerationStrategy());
        strategies.put("dayAndName", new DayAndNamePermalinkGenerationStrategy());
        strategies.put("monthAndName", new MonthAndNamePermalinkGenerationStrategy());
        strategies.put("numeric", new NumericPermalinkGenerationStrategy());
        strategies.put("postName", new PostNamePermalinkGenerationStrategy());
    }




    public String generatePermalink(String blogEntryTitle, Long blogEntryId, Instant creationDate, String countryIso) {
        String generationStrategyName = propertyService.getString(CommonKeys.PERMALINK_GENERATION_STRATEGY, countryIso, "default");
        PermalinkGenerationStrategy generationStrategy = strategies.get(generationStrategyName);
        if(generationStrategy == null) throw new PermalinkStrategyNotFoundException(generationStrategyName);

        return generationStrategy.generate(PermalinkForm.builder()
                .id(blogEntryId)
                .date(creationDate)
                .title(blogEntryTitle)
                .build());
    }
}
