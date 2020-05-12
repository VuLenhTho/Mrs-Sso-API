package com.vulenhtho.mrssso.dto.request;

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

    private Long colorId;

    private Long sizeId;

    private Long price;

    private Long quantity;

}
