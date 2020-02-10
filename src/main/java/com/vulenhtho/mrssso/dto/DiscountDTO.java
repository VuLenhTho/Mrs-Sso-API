package com.vulenhtho.mrssso.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter@Setter
public class DiscountDTO {
    private Long id;

    private String name;

    private Long percent;

    private Long amount;

    private String content;

    private Instant startDate;

    private Instant endDate;

}
