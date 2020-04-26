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
public class ListProductPageResponse {

    private List<ProductWebWindowViewResponseDTO> products;

    private Integer totalPage;

    private Integer currentPage;

    private PageHeaderDTO header;

}
