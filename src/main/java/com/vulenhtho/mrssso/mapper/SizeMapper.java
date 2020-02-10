package com.vulenhtho.mrssso.mapper;

import com.vulenhtho.mrssso.dto.SizeDTO;
import com.vulenhtho.mrssso.entity.Size;
import com.vulenhtho.mrssso.repository.SizeRepository;
import com.vulenhtho.mrssso.util.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SizeMapper {

    private SizeRepository sizeRepository;

    public SizeMapper(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    public SizeDTO toDTO(Size size){
        SizeDTO sizeDTO = new SizeDTO();
        BeanUtils.refine(size, sizeDTO, BeanUtils::copyNonNull);
        return sizeDTO;
    }

    public Set<SizeDTO> toDTO(Set<Size> sizes){
        return sizes.stream().map(this::toDTO).collect(Collectors.toSet());
    }

    public Size toEntity(SizeDTO sizeDTO){
        return sizeRepository.getOne(sizeDTO.getId());
    }

    public Set<Size> toEntity(Set<SizeDTO> sizeDTOS){
        return sizeDTOS.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
