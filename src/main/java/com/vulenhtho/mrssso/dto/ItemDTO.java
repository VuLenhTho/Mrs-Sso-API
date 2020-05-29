package com.vulenhtho.mrssso.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {

    private Long productId;

    private String productName;

    private Long colorId;

    private String color;

    private String size;

    private Long sizeId;

    private String thumbnail;

    private Long price;

    private Long importPrice;

    private Long quantity;

}
