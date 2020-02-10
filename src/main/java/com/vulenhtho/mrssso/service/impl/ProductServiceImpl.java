package com.vulenhtho.mrssso.service.impl;

import com.vulenhtho.mrssso.config.Constant;
import com.vulenhtho.mrssso.dto.ProductDTO;
import com.vulenhtho.mrssso.dto.request.ProductFilterRequestDTO;
import com.vulenhtho.mrssso.entity.Product;
import com.vulenhtho.mrssso.entity.ProductColorSize;
import com.vulenhtho.mrssso.mapper.ProductMapper;
import com.vulenhtho.mrssso.repository.*;
import com.vulenhtho.mrssso.service.ProductService;
import com.vulenhtho.mrssso.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    private ProductMapper productMapper;

    private ProductColorSizeRepository productColorSizeRepository;

    private ColorRepository colorRepository;

    private DiscountRepository discountRepository;

    private SizeRepository sizeRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, ColorRepository colorRepository, DiscountRepository discountRepository, SizeRepository sizeRepository, CategoryRepository categoryRepository, ProductColorSizeRepository productColorSizeRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productColorSizeRepository = productColorSizeRepository;
        this.colorRepository = colorRepository;
        this.discountRepository = discountRepository;
        this.sizeRepository = sizeRepository;
    }

    @Override
    public ProductDTO create(ProductDTO productDTO) {
        Product newProduct = productMapper.toEntity(productDTO, new Product());
        Product result = productRepository.save(newProduct);

        if (!CollectionUtils.isEmpty(productDTO.getProductColorSizeDTOS())){
            List<ProductColorSize> productColorSizes = new ArrayList<>();

            productDTO.getProductColorSizeDTOS().forEach(data ->{
                ProductColorSize productColorSize = new ProductColorSize();
                productColorSize.setColor(colorRepository.getOne(data.getColorId()));
                productColorSize.setProduct(productRepository.getOne(result.getId()));
                productColorSize.setSize(sizeRepository.getOne(data.getSizeId()));
                productColorSize.setQuantity(data.getQuantity());

                productColorSizes.add(productColorSize);
            });
            productColorSizeRepository.saveAll(productColorSizes);
        }
        return productMapper.toDTO(result);
    }

    @Override
    public ProductDTO update(ProductDTO productDTO) {
        Product newProduct = productMapper.toEntity(productDTO, productRepository.findById(productDTO.getId()).get());
        Product result = productRepository.save(newProduct);

        if (!CollectionUtils.isEmpty(productDTO.getProductColorSizeDTOS())){
            productColorSizeRepository.deleteByProductId(result.getId());
            List<ProductColorSize> productColorSizes = new ArrayList<>();

            productDTO.getProductColorSizeDTOS().forEach(data ->{
                ProductColorSize productColorSize = new ProductColorSize();
                productColorSize.setColor(colorRepository.getOne(data.getColorId()));
                productColorSize.setProduct(productRepository.getOne(result.getId()));
                productColorSize.setSize(sizeRepository.getOne(data.getSizeId()));
                productColorSize.setQuantity(data.getQuantity());

                productColorSizes.add(productColorSize);
            });
            productColorSizeRepository.saveAll(productColorSizes);
        }
        return productMapper.toDTO(result);
    }

    @Override
    public ProductDTO findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(value -> productMapper.toDTO(value)).orElse(null);
    }

    @Override
    public Page<ProductDTO> getAllWihFilter(ProductFilterRequestDTO filterRequest) {
        return productRepository.findAll(ProductSpecification.filterProduct(filterRequest)
                , PageRequest.of(
                        filterRequest.getPage()
                        , filterRequest.getSize()
                        , sort(filterRequest.getSort())
                )).map(productMapper::toDTO);
    }

    private Sort sort(String typeDateSort) {
        if (typeDateSort != null) {
            switch (typeDateSort) {
                case Constant.DATE_DES:
                    return Sort.by("createdDate").descending();
                case Constant.DATE_ASC:
                    return Sort.by("createdDate").ascending();
                case Constant.MODIFIED_DES:
                    return Sort.by("lastModifiedDate").descending();
            }
        }
        return Sort.by("createdDate").descending();
    }

    @Override
    public boolean delete(Long id) {
        try {
            productRepository.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean delete(List<Long> ids) {
        return ids.stream().allMatch(this::delete);
    }


}
