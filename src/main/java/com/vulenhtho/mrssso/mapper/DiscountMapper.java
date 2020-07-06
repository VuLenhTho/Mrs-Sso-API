package com.vulenhtho.mrssso.mapper;

import com.vulenhtho.mrssso.dto.DiscountDTO;
import com.vulenhtho.mrssso.entity.Discount;
import com.vulenhtho.mrssso.repository.DiscountRepository;
import com.vulenhtho.mrssso.util.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DiscountMapper {

    private final DiscountRepository discountRepository;

    public DiscountMapper(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public DiscountDTO toDTO(Discount discount){
        DiscountDTO discountDTO = new DiscountDTO();
        BeanUtils.refine(discount, discountDTO, BeanUtils::copyNonNull);
        return discountDTO;
    }

    public Set<DiscountDTO> toDTO(Set<Discount> discounts){
        return discounts.stream().map(this::toDTO).collect(Collectors.toSet());
    }

    public Discount toEntity(DiscountDTO discountDTO){
        return discountRepository.getOne(discountDTO.getId());
    }

    public Set<Discount> toEntity(Set<DiscountDTO> discountDTOS){
        return discountDTOS.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
