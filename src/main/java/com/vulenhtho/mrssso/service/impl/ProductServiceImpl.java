package com.vulenhtho.mrssso.service.impl;

import com.vulenhtho.mrssso.config.Constant;
import com.vulenhtho.mrssso.dto.*;
import com.vulenhtho.mrssso.dto.request.ProductFilterRequestDTO;
import com.vulenhtho.mrssso.dto.response.*;
import com.vulenhtho.mrssso.entity.Discount;
import com.vulenhtho.mrssso.entity.Product;
import com.vulenhtho.mrssso.entity.ProductColorSize;
import com.vulenhtho.mrssso.mapper.*;
import com.vulenhtho.mrssso.repository.*;
import com.vulenhtho.mrssso.service.ProductService;
import com.vulenhtho.mrssso.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final ProductColorSizeRepository productColorSizeRepository;

    private final ProductColorSizeMapper productColorSizeMapper;

    private final ColorRepository colorRepository;

    private final DiscountRepository discountRepository;

    private final DiscountMapper discountMapper;

    private final SizeRepository sizeRepository;

    private final WelcomeSlideRepository welcomeSlideRepository;

    private final WelcomeSlideMapper welcomeSlideMapper;

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final ColorMapper colorMapper;

    private final SizeMapper sizeMapper;

    private final SubCategoryRepository subCategoryRepository;

    private final SubCategoryMapper subCategoryMapper;



    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, ProductColorSizeMapper productColorSizeMapper, ColorRepository colorRepository
            , DiscountRepository discountRepository, SizeRepository sizeRepository, SubCategoryRepository subCategoryRepository
            , ProductColorSizeRepository productColorSizeRepository, DiscountMapper discountMapper, WelcomeSlideRepository welcomeSlideRepository
            , WelcomeSlideMapper welcomeSlideMapper, CategoryRepository categoryRepository, CategoryMapper categoryMapper, ColorMapper colorMapper, SizeMapper sizeMapper, SubCategoryRepository subCategoryRepository1, SubCategoryMapper subCategoryMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productColorSizeMapper = productColorSizeMapper;
        this.productColorSizeRepository = productColorSizeRepository;
        this.colorRepository = colorRepository;
        this.discountRepository = discountRepository;
        this.sizeRepository = sizeRepository;
        this.discountMapper = discountMapper;
        this.welcomeSlideRepository = welcomeSlideRepository;
        this.welcomeSlideMapper = welcomeSlideMapper;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.colorMapper = colorMapper;
        this.sizeMapper = sizeMapper;
        this.subCategoryRepository = subCategoryRepository1;
        this.subCategoryMapper = subCategoryMapper;
    }

    @Override
    public Product create(ProductDTO productDTO) {
        Product newProduct = productMapper.toEntity(productDTO, new Product());
        return productRepository.save(newProduct);
    }

    @Override
    public void update(ProductDetailDTO productDetailDTO) {
        ProductDTO productDTO = productDetailDTO.getProductDTO();
        Product newProduct = productMapper.toEntity(productDTO, productRepository.findById(productDTO.getId()).get());
        productRepository.save(newProduct);

        if (!StringUtils.isEmpty(productDetailDTO.getProductColorSizeIdsToDel())) {
            List<Long> productColorSizeIdsToDel = Arrays.stream(productDetailDTO.getProductColorSizeIdsToDel().split(","))
                    .filter(st -> !StringUtils.isEmpty(st))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            for (Long id : productColorSizeIdsToDel) {
                productColorSizeRepository.deleteById(id);
            }
        }

    }

    @Override
    public ProductDTO findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(productMapper::toDTO).orElse(null);
    }

    @Override
    public ProductWebResponseDTO findForWebById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        ProductWebResponseDTO productWebResponseDTO = product.map(productMapper::toWebResponseDTO).orElse(new ProductWebResponseDTO());
        productWebResponseDTO.setHeader(getHeaderResponse());
        return productWebResponseDTO;
    }

    @Override
    public ListProductPageResponse getWindowViewByFilterForWeb(ProductFilterRequestDTO filterRequest) {
        Page<ProductWebWindowViewResponseDTO> products = productRepository.findAll(ProductSpecification.filterProduct(filterRequest)
                , PageRequest.of(
                        filterRequest.getPage()
                        , filterRequest.getSize()
                        , sort(filterRequest.getSort())
                )).map(productMapper::toWebWindowViewResponseDTO);
        return new ListProductPageResponse(products.getContent(), products.getTotalPages(), products.getNumber(), getHeaderResponse());
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
                case Constant.HOT_DES:
                    return Sort.by("hot").descending();
                case Constant.PRICE_ASC:
                    return Sort.by("price").ascending();
                case Constant.PRICE_DES:
                    return Sort.by("price").descending();
            }
        }
        return Sort.by("createdDate").descending();
    }

    @Override
    public boolean delete(Long id) {
        try {
            productColorSizeRepository.deleteByProductId(id);
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
        ListProductPageResponse productAndHeader = getWindowViewByFilterForWeb(filter);
        List<ProductWebWindowViewResponseDTO> hotProduct = productAndHeader.getProducts();
        filter.setHot(null);
        filter.setTrend(true);
        List<ProductWebWindowViewResponseDTO> trendProduct = getWindowViewByFilterForWeb(filter).getProducts();

        List<WelcomeSlideDTO> welcomeSlides = welcomeSlideMapper.toDTO(welcomeSlideRepository.getByIsDisabled(false));
        return new WebHomeResponse(hotProduct, trendProduct, welcomeSlides, productAndHeader.getHeader());
    }

    public PageHeaderDTO getHeaderResponse() {
        List<CategoryDTO> categoryDTOS = categoryMapper.toDTO(categoryRepository.findAll(Sort.by("id").ascending()));
        List<String> discounts = discountRepository.getByInTimeDiscount(Instant.now()).stream().map(Discount::getName).collect(Collectors.toList());
        return new PageHeaderDTO(categoryDTOS, discounts);
    }


    @Override
    public ProductDetailDTO getProductDetailByAdmin(Long id) {
        ProductDTO productDTO = productMapper.toDTO(productRepository.findById(id).get());
        Set<ColorDTO> colorDTOS = colorMapper.toDTO(new HashSet<>(colorRepository.findAll()));
        Set<SizeDTO> sizeDTOS = sizeMapper.toDTO(new HashSet<>(sizeRepository.findAll()));
        Set<SubCategoryDTO> subCategoryDTOS = subCategoryMapper.toDTO(new HashSet<>(subCategoryRepository.findAll()));
        Set<DiscountDTO> discountDTOS = discountMapper.toDTO(discountRepository.getByInTimeDiscountAndForProduct(Instant.now()));

        return new ProductDetailDTO(productDTO, subCategoryDTOS, colorDTOS, sizeDTOS, discountDTOS);
    }

    @Override
    public InfoToCreateProductDTO getInfoToCreateProductDTO() {
        Set<SubCategoryDTO> subCategoryDTOS = subCategoryMapper.toDTO(new HashSet<>(subCategoryRepository.findAll()));
        Set<DiscountDTO> discountDTOS = discountMapper.toDTO(discountRepository.getByInTimeDiscountAndForProduct(Instant.now()));
        return new InfoToCreateProductDTO(discountDTOS, subCategoryDTOS);
    }

    @Override
    public void addProductColorSize(ProductColorSizeDTO productColorSizeDTO) {
        ProductColorSize productColorSize = productColorSizeMapper.toEntity(productColorSizeDTO);

        ProductColorSize checkExist = productColorSizeRepository.findByColorAndSizeAndProduct(productColorSize.getColor()
                , productColorSize.getSize(), productColorSize.getProduct());
        if (checkExist != null) {
            checkExist.setQuantity(productColorSize.getQuantity());
            productColorSizeRepository.save(checkExist);
        } else {
            productColorSizeRepository.save(productColorSize);
        }
    }


}
