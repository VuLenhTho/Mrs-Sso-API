package com.vulenhtho.mrssso.mapper;

import com.vulenhtho.mrssso.dto.DiscountDTO;
import com.vulenhtho.mrssso.dto.ProductDTO;
import com.vulenhtho.mrssso.dto.response.ProductWebResponseDTO;
import com.vulenhtho.mrssso.dto.response.ProductWebWindowViewResponseDTO;
import com.vulenhtho.mrssso.entity.Product;
import com.vulenhtho.mrssso.entity.ProductColorSize;
import com.vulenhtho.mrssso.repository.ColorRepository;
import com.vulenhtho.mrssso.repository.DiscountRepository;
import com.vulenhtho.mrssso.repository.SizeRepository;
import com.vulenhtho.mrssso.repository.SubCategoryRepository;
import com.vulenhtho.mrssso.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Component
public class ProductMapper {
    private ColorRepository colorRepository;

    private DiscountRepository discountRepository;

    private SizeRepository sizeRepository;

    private SubCategoryRepository subCategoryRepository;

    private ProductColorSizeMapper productColorSizeMapper;

    private ColorMapper colorMapper;

    private SizeMapper sizeMapper;

    private DiscountMapper discountMapper;

    private SubCategoryMapper subCategoryMapper;

    @Autowired
    public ProductMapper(ColorRepository colorRepository, DiscountRepository discountRepository, SizeRepository sizeRepository, SubCategoryRepository subCategoryRepository, ProductColorSizeMapper productColorSizeMapper, ColorMapper colorMapper, SizeMapper sizeMapper, DiscountMapper discountMapper, SubCategoryMapper subCategoryMapper) {
        this.colorRepository = colorRepository;
        this.discountRepository = discountRepository;
        this.sizeRepository = sizeRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.productColorSizeMapper = productColorSizeMapper;
        this.colorMapper = colorMapper;
        this.sizeMapper = sizeMapper;
        this.discountMapper = discountMapper;
        this.subCategoryMapper = subCategoryMapper;
    }

    public Product toEntity(ProductDTO productDTO, Product product) {
        BeanUtils.refine(productDTO, product, BeanUtils::copyNonNull);
        product.setSubCategory(subCategoryRepository.getOne(productDTO.getSubCategoryDTO().getId()));

        if (!CollectionUtils.isEmpty(productDTO.getColorDTOS())) {
            product.setColors(new HashSet<>());
            productDTO.getColorDTOS().forEach(colorDTO -> {
                product.getColors().add(colorRepository.getOne(colorDTO.getId()));
            });
        }
        if (!CollectionUtils.isEmpty(productDTO.getDiscountDTOS())) {
            product.setDiscounts(new HashSet<>());
            productDTO.getDiscountDTOS().forEach(discountDTO -> {
                product.getDiscounts().add(discountRepository.getOne(discountDTO.getId()));
            });
        }

        if (!CollectionUtils.isEmpty(productDTO.getSizeDTOS())) {
            product.setSizes(new HashSet<>());
            productDTO.getSizeDTOS().forEach(sizeDTO -> {
                product.getSizes().add(sizeRepository.getOne(sizeDTO.getId()));
            });
        }

        if (!CollectionUtils.isEmpty(productDTO.getProductColorSizeDTOS())) {
            product.setProductColorSizes(new HashSet<>());
            productDTO.getProductColorSizeDTOS().forEach(productColorSizeDTO -> {
                ProductColorSize productColorSize = new ProductColorSize();
                productColorSize.setQuantity(productColorSizeDTO.getQuantity());
                productColorSize.setSize(sizeRepository.getOne(productColorSizeDTO.getSizeId()));
                productColorSize.setColor(colorRepository.getOne(productColorSizeDTO.getColorId()));

                product.getProductColorSizes().add(productColorSize);
            });
        }
        return product;
    }

    public ProductDTO toDTO(Product product) {
        if (product == null) return null;
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.refine(product, productDTO, BeanUtils::copyNonNull);

        productDTO.setSubCategoryDTO(subCategoryMapper.toDTO(product.getSubCategory()));

        if (!CollectionUtils.isEmpty(product.getProductColorSizes())) {
            productDTO.setProductColorSizeDTOS(productColorSizeMapper.toDTO(product.getProductColorSizes()));
        }
        if (!CollectionUtils.isEmpty(product.getColors())) {
            productDTO.setColorDTOS(colorMapper.toDTO(product.getColors()));
        }
        if (!CollectionUtils.isEmpty(product.getSizes())) {
            productDTO.setSizeDTOS(sizeMapper.toDTO(product.getSizes()));
        }
        if (!CollectionUtils.isEmpty(product.getDiscounts())) {
            productDTO.setDiscountDTOS(discountMapper.toDTO(product.getDiscounts()));
            boolean hasDiscount = productDTO.getDiscountDTOS().stream().anyMatch(this::isInDiscountTimeAndForProduct);
            if (hasDiscount) {
                productDTO.setPrice(countPriceInDiscount(productDTO.getDiscountDTOS(), product.getPrice()));
                productDTO.setOriginalPrice(product.getPrice());
            }
        }

        return productDTO;
    }

    public ProductWebResponseDTO toWebResponseDTO(Product product) {
        if (product == null) return null;

        ProductWebResponseDTO productWebResponseDTO = new ProductWebResponseDTO();
        BeanUtils.refine(toDTO(product), productWebResponseDTO, BeanUtils::copyNonNull);

        return productWebResponseDTO;
    }

    public ProductWebWindowViewResponseDTO toWebWindowViewResponseDTO(Product product) {
        if (product == null) return null;
        ProductWebWindowViewResponseDTO responseDTO = new ProductWebWindowViewResponseDTO();
        Set<DiscountDTO> discountDTOS = discountMapper.toDTO(product.getDiscounts());
        BeanUtils.refine(product, responseDTO, BeanUtils::copyNonNull);

        if (!CollectionUtils.isEmpty(discountDTOS)) {
            boolean hasDiscount = discountDTOS.stream().anyMatch(this::isInDiscountTimeAndForProduct);
            if (hasDiscount) {
                responseDTO.setPrice(countPriceInDiscount(discountDTOS, product.getPrice()));
                responseDTO.setOriginalPrice(product.getPrice());
                responseDTO.setIsDiscount(true);
            }
        }
        return responseDTO;
    }

    private Long countPriceInDiscount(Set<DiscountDTO> discountDTOS, Long currentPrice) {
        Long finalPrice = currentPrice;
        for (DiscountDTO discountDTO : discountDTOS) {
            if (isInDiscountTimeAndForProduct(discountDTO)) {
                if (discountDTO.getAmount() != null) {
                    finalPrice -= discountDTO.getAmount();
                }
                if (discountDTO.getPercent() != null) {
                    finalPrice -= (currentPrice * discountDTO.getPercent() / 100);
                }
            }

        }
        return finalPrice;
    }

    private boolean isInDiscountTimeAndForProduct(DiscountDTO discountDTO) {
        Instant now = Instant.now();
        return now.isAfter(discountDTO.getStartDate()) && now.isBefore(discountDTO.getEndDate()) && discountDTO.getIsForProduct();
    }
}
