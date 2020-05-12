package com.vulenhtho.mrssso.mapper;

import com.vulenhtho.mrssso.dto.ProductColorSizeDTO;
import com.vulenhtho.mrssso.entity.ProductColorSize;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductColorSizeMapper {

    public ProductColorSizeDTO toDTO(ProductColorSize productColorSize){
        if (productColorSize == null) return null;
        ProductColorSizeDTO productColorSizeDTO = new ProductColorSizeDTO();
        productColorSizeDTO.setColorId(productColorSize.getColor().getId());
        productColorSizeDTO.setProductId(productColorSize.getProduct().getId());
        productColorSizeDTO.setSizeId(productColorSize.getSize().getId());
        productColorSizeDTO.setQuantity(productColorSize.getQuantity());

        return productColorSizeDTO;
    }

    public Set<ProductColorSizeDTO> toDTO(Set<ProductColorSize> productColorSizes){
        if (productColorSizes == null) return null;
        return productColorSizes.stream().map(this::toDTO).collect(Collectors.toSet());
    }
}
