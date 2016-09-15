package com.druidkuma.blog.web.transformer;

import com.druidkuma.blog.dao.country.LanguageRepository;
import com.druidkuma.blog.domain.country.Language;
import com.druidkuma.blog.web.dto.country.LanguageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Iurii Miedviediev
 *
 * @author DruidKuma
 * @version 1.0.0
 * @since 9/8/16
 */
@Component
public class LanguageTransformer implements DtoTransformer<Language, LanguageDto> {

    @Autowired
    private LanguageRepository languageRepository;

    @Override
    public Language transformFromDto(LanguageDto dto) {
        Language language = languageRepository.findByIsoCode(dto.getIsoCode());
        if(language == null) {
            language = Language.builder().isoCode(dto.getIsoCode()).build();
        }
        language.setName(dto.getName());
        language.setNativeName(dto.getNativeName());
        return language;
    }

    @Override
    public LanguageDto tranformToDto(Language entity) {
        return LanguageDto
                .builder()
                .isoCode(entity.getIsoCode())
                .name(entity.getName())
                .nativeName(entity.getNativeName())
                .build();
    }
}
