package com.vulenhtho.mrssso.dto;

import com.vulenhtho.mrssso.entity.enumeration.BillStatus;
import com.vulenhtho.mrssso.entity.enumeration.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class BillDTO {

    private Long id;

    private String receiver;

    private String phone;

    private String address;

    private Long shippingCosts;

    private Long finalPayMoney;

    private Long totalImportMoney;

    private Long totalMoney;

    private PaymentMethod paymentMethod;

    private String paymentInfo;

    private String note;

    private BillStatus status;

    private Long userId;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<ItemDTO> itemDTOS = new HashSet<>();
}
