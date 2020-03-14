package com.vulenhtho.mrssso.service.impl;

import com.vulenhtho.mrssso.config.Constant;
import com.vulenhtho.mrssso.dto.CategoryDTO;
import com.vulenhtho.mrssso.dto.ProductColorSizeDTO;
import com.vulenhtho.mrssso.dto.ProductDTO;
import com.vulenhtho.mrssso.dto.WelcomeSlideDTO;
import com.vulenhtho.mrssso.dto.request.ProductFilterRequestDTO;
import com.vulenhtho.mrssso.dto.response.ProductWebResponseDTO;
import com.vulenhtho.mrssso.dto.response.ProductWebWindowViewResponseDTO;
import com.vulenhtho.mrssso.dto.response.WebHomeResponse;
import com.vulenhtho.mrssso.entity.Product;
import com.vulenhtho.mrssso.entity.ProductColorSize;
import com.vulenhtho.mrssso.mapper.CategoryMapper;
import com.vulenhtho.mrssso.mapper.ProductMapper;
import com.vulenhtho.mrssso.mapper.WelcomeSlideMapper;
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
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    private ProductMapper productMapper;

    private ProductColorSizeRepository productColorSizeRepository;

    private ColorRepository colorRepository;

    private DiscountRepository discountRepository;

    private SizeRepository sizeRepository;

    private WelcomeSlideRepository welcomeSlideRepository;

    private WelcomeSlideMapper welcomeSlideMapper;

    private CategoryRepository categoryRepository;

    private CategoryMapper categoryMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, ColorRepository colorRepository, DiscountRepository discountRepository, SizeRepository sizeRepository, SubCategoryRepository subCategoryRepository, ProductColorSizeRepository productColorSizeRepository, WelcomeSlideRepository welcomeSlideRepository, WelcomeSlideMapper welcomeSlideMapper, CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productColorSizeRepository = productColorSizeRepository;
        this.colorRepository = colorRepository;
        this.discountRepository = discountRepository;
        this.sizeRepository = sizeRepository;
        this.welcomeSlideRepository = welcomeSlideRepository;
        this.welcomeSlideMapper = welcomeSlideMapper;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public ProductDTO create(ProductDTO productDTO) {
        Product newProduct = productMapper.toEntity(productDTO, new Product());
        Product result = productRepository.save(newProduct);

        if (!CollectionUtils.isEmpty(productDTO.getProductColorSizeDTOS())){
            List<ProductColorSize> productColorSizes = getByProductColorSizeDTOS(productDTO.getProductColorSizeDTOS(), result.getId());
            productColorSizeRepository.saveAll(productColorSizes);
        }
        return productMapper.toDTO(result);
    }

    @Override
    public ProductDTO update(ProductDTO productDTO) {
        Product newProduct = productMapper.toEntity(productDTO, productRepository.getOne(productDTO.getId()));
        Product result = productRepository.save(newProduct);

        if (!CollectionUtils.isEmpty(productDTO.getProductColorSizeDTOS())) {
            productColorSizeRepository.deleteByProductId(result.getId());
            List<ProductColorSize> productColorSizes = getByProductColorSizeDTOS(productDTO.getProductColorSizeDTOS(), result.getId());
            productColorSizeRepository.saveAll(productColorSizes);
        }
        return productMapper.toDTO(result);
    }

    public List<ProductColorSize> getByProductColorSizeDTOS(Set<ProductColorSizeDTO> productColorSizeDTOS, Long productId) {
        List<ProductColorSize> productColorSizes = new ArrayList<>();
        productColorSizeDTOS.forEach(data -> {
            ProductColorSize productColorSize = new ProductColorSize();
            productColorSize.setColor(colorRepository.getOne(data.getColorId()));
            productColorSize.setProduct(productRepository.getOne(productId));
            productColorSize.setSize(sizeRepository.getOne(data.getSizeId()));
            productColorSize.setQuantity(data.getQuantity());

            productColorSizes.add(productColorSize);
        });
        return productColorSizes;
    }

    @Override
    public ProductDTO findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(value -> productMapper.toDTO(value)).orElse(null);
    }

    @Override
    public ProductWebResponseDTO findForWebById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(value -> productMapper.toWebResponseDTO(value)).orElse(null);
    }

    @Override
    public Page<ProductWebWindowViewResponseDTO> getWindowViewByFilterForWeb(ProductFilterRequestDTO filterRequest) {
        return productRepository.findAll(ProductSpecification.filterProduct(filterRequest)
                , PageRequest.of(
                        filterRequest.getPage()
                        , filterRequest.getSize()
                        , sort(filterRequest.getSort())
                )).map(productMapper::toWebWindowViewResponseDTO);
    }

    @Override
    public Page<ProductWebResponseDTO> getAllWithFilterForWeb(ProductFilterRequestDTO filterRequest) {
        return productRepository.findAll(ProductSpecification.filterProduct(filterRequest)
                , PageRequest.of(
                        filterRequest.getPage()
                        , filterRequest.getSize()
                        , sort(filterRequest.getSort())
                )).map(productMapper::toWebResponseDTO);
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
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(List<Long> ids) {
        return ids.stream().allMatch(this::delete);
    }

    @Override
    public WebHomeResponse getDataForWebHomePage() {
        ProductFilterRequestDTO filter = new ProductFilterRequestDTO(true, null, 0, 16);
        List<ProductWebWindowViewResponseDTO> hotProduct = getWindowViewByFilterForWeb(filter).getContent();
        filter.setHot(null);
        filter.setTrend(true);
        List<ProductWebWindowViewResponseDTO> trendProduct = getWindowViewByFilterForWeb(filter).getContent();

        List<WelcomeSlideDTO> welcomeSlides = welcomeSlideMapper.toDTO(welcomeSlideRepository.getByIsDisabled(false));
        List<CategoryDTO> categoryDTOS = categoryMapper.toDTO(categoryRepository.findAll());

        return new WebHomeResponse(hotProduct, trendProduct, welcomeSlides, categoryDTOS);
    }


}
