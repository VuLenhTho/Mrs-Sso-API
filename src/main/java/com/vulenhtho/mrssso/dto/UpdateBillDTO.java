package com.vulenhtho.mrssso.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBillDTO {
    private BillDTO billDTO;

    private String productIdsToDel;

    private String productIds;

    private String quantityOfProducts;
}
