package com.vulenhtho.mrssso.mapper;

import com.vulenhtho.mrssso.dto.WelcomeSlideDTO;
import com.vulenhtho.mrssso.entity.WelcomeSlide;
import com.vulenhtho.mrssso.util.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WelcomeSlideMapper {
    public WelcomeSlideDTO toDTO(WelcomeSlide welcomeSlide) {
        if (welcomeSlide == null) return null;
        WelcomeSlideDTO welcomeSlideDTO = new WelcomeSlideDTO();
        BeanUtils.refine(welcomeSlide, welcomeSlideDTO, BeanUtils::copyNonNull);
        return welcomeSlideDTO;
    }

    public List<WelcomeSlideDTO> toDTO(List<WelcomeSlide> welcomeSlides) {
        if (welcomeSlides == null) return null;
        return welcomeSlides.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public WelcomeSlide toEntity(WelcomeSlideDTO welcomeSlideDTO) {
        if (welcomeSlideDTO == null) return null;
        WelcomeSlide welcomeSlide = new WelcomeSlide();
        BeanUtils.refine(welcomeSlideDTO, welcomeSlide, BeanUtils::copyNonNull);
        return welcomeSlide;
    }

    public List<WelcomeSlide> toEntity(List<WelcomeSlideDTO> welcomeSlideDTOS) {
        if (welcomeSlideDTOS == null) return null;
        return welcomeSlideDTOS.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
