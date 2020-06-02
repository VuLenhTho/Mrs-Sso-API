package com.vulenhtho.mrssso.mapper;

import com.vulenhtho.mrssso.dto.ProductColorSizeDTO;
import com.vulenhtho.mrssso.entity.ProductColorSize;
import com.vulenhtho.mrssso.repository.ColorRepository;
import com.vulenhtho.mrssso.repository.ProductRepository;
import com.vulenhtho.mrssso.repository.SizeRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductColorSizeMapper {

    private final ColorRepository colorRepository;

    private final SizeRepository sizeRepository;

    private final ProductRepository productRepository;

    public ProductColorSizeMapper(ColorRepository colorRepository, SizeRepository sizeRepository, ProductRepository productRepository) {
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.productRepository = productRepository;
    }

    public ProductColorSizeDTO toDTO(ProductColorSize productColorSize) {
        if (productColorSize == null) return null;
        ProductColorSizeDTO productColorSizeDTO = new ProductColorSizeDTO();
        productColorSizeDTO.setId(productColorSize.getId());
        productColorSizeDTO.setColorId(productColorSize.getColor().getId());
        productColorSizeDTO.setProductId(productColorSize.getProduct().getId());
        productColorSizeDTO.setSizeId(productColorSize.getSize().getId());
        productColorSizeDTO.setQuantity(productColorSize.getQuantity());
        productColorSizeDTO.setColor(productColorSize.getColor().getName());
        productColorSizeDTO.setSize(productColorSize.getSize().getName());

        return productColorSizeDTO;
    }

    public Set<ProductColorSizeDTO> toDTO(Set<ProductColorSize> productColorSizes) {
        if (productColorSizes == null) return null;
        return productColorSizes.stream().map(this::toDTO).collect(Collectors.toSet());
    }

    public ProductColorSize toEntity(ProductColorSizeDTO productColorSizeDTO) {
        ProductColorSize productColorSize = new ProductColorSize();
        productColorSize.setId(productColorSizeDTO.getId());
        productColorSize.setColor(colorRepository.findById(productColorSizeDTO.getColorId()).get());
        productColorSize.setSize(sizeRepository.findById(productColorSizeDTO.getSizeId()).get());
        productColorSize.setProduct(productRepository.findById(productColorSizeDTO.getProductId()).get());
        productColorSize.setQuantity(productColorSizeDTO.getQuantity());

        return productColorSize;
    }
}
