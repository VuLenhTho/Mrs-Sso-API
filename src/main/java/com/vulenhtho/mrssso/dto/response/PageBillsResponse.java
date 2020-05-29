package com.vulenhtho.mrssso.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageBillsResponse {
    private List<ShortInfoBillResponse> billDTOS;

    private Integer totalPages;

    private Integer currentPage;
}
