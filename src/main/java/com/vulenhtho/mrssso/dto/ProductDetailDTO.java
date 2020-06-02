package com.vulenhtho.mrssso.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDTO {

    private ProductDTO productDTO;

    private Set<SubCategoryDTO> subCategoryDTOS;

    private Set<ColorDTO> colorDTOS;

    private Set<SizeDTO> sizeDTOS;

    private String productColorSizeIdsToDel;

    public ProductDetailDTO(ProductDTO productDTO, Set<SubCategoryDTO> subCategoryDTOS, Set<ColorDTO> colorDTOS, Set<SizeDTO> sizeDTOS) {
        this.productDTO = productDTO;
        this.subCategoryDTOS = subCategoryDTOS;
        this.colorDTOS = colorDTOS;
        this.sizeDTOS = sizeDTOS;
    }
}
