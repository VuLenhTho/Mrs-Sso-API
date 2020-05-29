package com.vulenhtho.mrssso.dto.request;

import com.vulenhtho.mrssso.entity.enumeration.BillStatus;
import com.vulenhtho.mrssso.entity.enumeration.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillFilterRequest {

    private String sort;

    private BillStatus status;

    private String search;

    private PaymentMethod paymentMethod;

    private Integer page;

    private Integer size;

    public BillFilterRequest(String sort, String search, Integer page, Integer size) {
        this.sort = sort;
        this.search = search;
        this.page = page;
        this.size = size;
    }
}
