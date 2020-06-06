package com.vulenhtho.mrssso.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDTO {

    private ProductDTO productDTO;

    private Set<SubCategoryDTO> subCategoryDTOS = new HashSet<>();

    private Set<ColorDTO> colorDTOS;

    private Set<SizeDTO> sizeDTOS;

    private String productColorSizeIdsToDel;

    private Set<DiscountDTO> discountDTOS = new HashSet<>();

    public ProductDetailDTO(ProductDTO productDTO, Set<SubCategoryDTO> subCategoryDTOS, Set<ColorDTO> colorDTOS, Set<SizeDTO> sizeDTOS, Set<DiscountDTO> discountDTOS) {
        this.productDTO = productDTO;
        this.subCategoryDTOS = subCategoryDTOS;
        this.colorDTOS = colorDTOS;
        this.sizeDTOS = sizeDTOS;
        this.discountDTOS = discountDTOS;
    }
}
