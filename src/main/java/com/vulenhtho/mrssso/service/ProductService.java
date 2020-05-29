package com.vulenhtho.mrssso.service;

import com.vulenhtho.mrssso.dto.ProductDTO;
import com.vulenhtho.mrssso.dto.request.ProductFilterRequestDTO;
import com.vulenhtho.mrssso.dto.response.ListProductPageResponse;
import com.vulenhtho.mrssso.dto.response.PageHeaderDTO;
import com.vulenhtho.mrssso.dto.response.ProductWebResponseDTO;
import com.vulenhtho.mrssso.dto.response.WebHomeResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductDTO create(ProductDTO productDTO);

    ProductDTO update(ProductDTO productDTO);

    ProductWebResponseDTO findForWebById(Long id);

    ListProductPageResponse getWindowViewByFilterForWeb(ProductFilterRequestDTO filterRequest);

    Page<ProductWebResponseDTO> getAllWithFilterForWeb(ProductFilterRequestDTO filterRequest);

    ProductDTO findById(Long id);

    Page<ProductDTO> getAllWihFilter(ProductFilterRequestDTO filterRequest);

    boolean delete(Long id);

    boolean delete(List<Long> ids);

    WebHomeResponse getDataForWebHomePage();

    PageHeaderDTO getHeaderResponse();

}
