package com.vulenhtho.mrssso.mapper;

import com.vulenhtho.mrssso.dto.ColorDTO;
import com.vulenhtho.mrssso.entity.Color;
import com.vulenhtho.mrssso.repository.ColorRepository;
import com.vulenhtho.mrssso.util.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ColorMapper {
    private final ColorRepository colorRepository;

    public ColorMapper(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @PreDestroy
    public void postConstruct() {
        System.out.println("Đối lượng colorMapper trước khi bị xóa sẽ chạy hàm này");
    }

    public ColorDTO toDTO(Color color) {
        ColorDTO colorDTO = new ColorDTO();
        BeanUtils.refine(color, colorDTO, BeanUtils::copyNonNull);
        return colorDTO;
    }

    public Set<ColorDTO> toDTO(Set<Color> colors) {
        return colors.stream().map(this::toDTO).collect(Collectors.toSet());
    }

    public Color toEntity(ColorDTO colorDTO){
        return colorRepository.getOne(colorDTO.getId());
    }

    public Set<Color> toEntity(Set<ColorDTO> colorDTOS){
        return colorDTOS.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
