package com.vulenhtho.mrssso.dto.request;


import com.vulenhtho.mrssso.entity.enumeration.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class CartDTO {

    private String receiver;

    private String phone;

    private String address;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String accountName;

    private String accountNumber;

    private Long totalImportMoney;

    private Long totalMoney;

    private Long finalPayMoney;

    private String note;

    private Set<ItemDTO> itemList = new HashSet<>();

}
