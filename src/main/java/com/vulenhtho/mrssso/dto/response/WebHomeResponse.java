package com.vulenhtho.mrssso.dto.response;

import com.vulenhtho.mrssso.dto.WelcomeSlideDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebHomeResponse {

    private List<ProductWebWindowViewResponseDTO> hotProductList = new ArrayList<>();

    private List<ProductWebWindowViewResponseDTO> trendProductList = new ArrayList<>();

    private List<WelcomeSlideDTO> welcomeSlideDTOS = new ArrayList<>();

    private PageHeaderDTO headerResponse;

}
