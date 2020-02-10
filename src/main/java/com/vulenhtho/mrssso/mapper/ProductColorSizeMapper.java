package com.vulenhtho.mrssso.mapper;

import com.vulenhtho.mrssso.dto.ProductColorSizeDTO;
import com.vulenhtho.mrssso.entity.ProductColorSize;
import com.vulenhtho.mrssso.util.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductColorSizeMapper {

    public ProductColorSizeDTO toDTO(ProductColorSize productColorSize){
        ProductColorSizeDTO productColorSizeDTO = new ProductColorSizeDTO();
        BeanUtils.refine(productColorSize, productColorSizeDTO, BeanUtils::copyNonNull);
        return productColorSizeDTO;
    }

    public Set<ProductColorSizeDTO> toDTO(Set<ProductColorSize> productColorSizes){
        return productColorSizes.stream().map(this::toDTO).collect(Collectors.toSet());
    }
}
