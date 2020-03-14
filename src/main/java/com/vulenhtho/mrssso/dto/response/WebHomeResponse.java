package com.vulenhtho.mrssso.dto.response;

import com.vulenhtho.mrssso.dto.CategoryDTO;
import com.vulenhtho.mrssso.dto.WelcomeSlideDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class WebHomeResponse {

    private List<ProductWebWindowViewResponseDTO> hotProductList = new ArrayList<>();

    private List<ProductWebWindowViewResponseDTO> trendProductList = new ArrayList<>();

    private List<WelcomeSlideDTO> welcomeSlideDTOS = new ArrayList<>();

    private List<CategoryDTO> categoryDTOS = new ArrayList<>();

}
