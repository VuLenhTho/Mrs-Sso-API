package com.vulenhtho.mrssso.service;

import com.vulenhtho.mrssso.dto.ProductDTO;
import com.vulenhtho.mrssso.dto.request.ProductFilterRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductDTO create(ProductDTO productDTO);

    ProductDTO update(ProductDTO productDTO);

    ProductDTO findById(Long id);

    Page<ProductDTO> getAllWihFilter(ProductFilterRequestDTO filterRequest);

    boolean delete(Long id);

    boolean delete(List<Long> ids);
}
