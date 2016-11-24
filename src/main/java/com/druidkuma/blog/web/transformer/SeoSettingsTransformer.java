package com.druidkuma.blog.web.transformer;

import com.druidkuma.blog.dao.seo.SeoSettingsRepository;
import com.druidkuma.blog.domain.entry.SeoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 11/24/16
 */
@Component
public class SeoSettingsTransformer implements DtoTransformer<SeoSettings, SeoSettings> {

    @Autowired
    private SeoSettingsRepository seoSettingsRepository;

    @Override
    public SeoSettings transformFromDto(SeoSettings dto) {
        SeoSettings settings = dto.getId() == null ? new SeoSettings() : seoSettingsRepository.findOne(dto.getId());

        settings.setTitle(dto.getTitle());
        settings.setDescription(dto.getDescription());
        settings.setKeywords(dto.getKeywords());
        settings.setMetaRobotsNoFollow(dto.getMetaRobotsNoFollow());
        settings.setMetaRobotsNoIndex(dto.getMetaRobotsNoIndex());
        settings.setOgDescription(dto.getOgDescription());
        settings.setOgImage(dto.getOgImage());
        settings.setOgTitle(dto.getOgTitle());
        settings.setOgType(dto.getOgType());

        return settings;
    }

    @Override
    public SeoSettings tranformToDto(SeoSettings entity) {
        return entity;
    }
}
