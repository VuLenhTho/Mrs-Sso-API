package com.vulenhtho.mrssso.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddAnItemIntoBillDTO {
    private String productInfo;

    private String colorInfo;

    private String sizeInfo;

    private Long quantity;

    private Long billId;
}
