package com.vulenhtho.mrssso.dto;

import com.vulenhtho.mrssso.entity.enumeration.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ProductDTO {
    private Long id;

    private String name;

    private Long importPrice;

    private Long price;

    private String shortDescription;

    private ProductStatus status;

    private String thumbnail;

    private String photoList;

    private Boolean hot;

    private Boolean trend;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private SubCategoryDTO subCategoryDTO;

    private Set<ProductColorSizeDTO> productColorSizeDTOS = new HashSet<>();

    private Set<ColorDTO> colorDTOS = new HashSet<>();

    private Set<DiscountDTO> discountDTOS = new HashSet<>();

    private Set<SizeDTO> sizeDTOS = new HashSet<>();

}
