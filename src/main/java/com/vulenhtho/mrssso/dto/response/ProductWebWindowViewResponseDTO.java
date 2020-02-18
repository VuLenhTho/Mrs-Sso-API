package com.vulenhtho.mrssso.dto.response;

import com.vulenhtho.mrssso.entity.enumeration.ProductStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductWebWindowViewResponseDTO {
    private Long id;

    private String name;

    private Long price;

    private ProductStatus status;

    private String thumbnail;

    private Boolean hot;

    private Boolean trend;

    private Boolean isDiscount;

}
