package com.vulenhtho.mrssso.service;

import com.vulenhtho.mrssso.dto.ProductDTO;
import com.vulenhtho.mrssso.dto.request.CartDTO;
import com.vulenhtho.mrssso.dto.request.ItemDTO;
import com.vulenhtho.mrssso.dto.request.ProductFilterRequestDTO;
import com.vulenhtho.mrssso.dto.response.*;
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

    ItemsForCartAndHeader getItemShowInCart(List<ItemDTO> itemDTOS);

    void createBill(CartDTO cartDTO);
}
