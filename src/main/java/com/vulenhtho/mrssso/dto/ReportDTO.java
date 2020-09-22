package com.vulenhtho.mrssso.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    private Integer year;

    private Integer month;

    private Long importMoney;

    private Long interestMoney;

    private Long moneyFromSale;

    private List<ProductInfoToReportDTO> bestsellerProduct = new ArrayList<>();

    private List<ProductInfoToReportDTO> badSellerProduct = new ArrayList<>();

    public ReportDTO(Integer year, Integer month, Long importMoney, Long interestMoney, Long moneyFromSale) {
        this.year = year;
        this.month = month;
        this.importMoney = importMoney;
        this.interestMoney = interestMoney;
        this.moneyFromSale = moneyFromSale;
    }
}
