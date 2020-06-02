package com.vulenhtho.mrssso.mapper;

import com.vulenhtho.mrssso.dto.DiscountDTO;
import com.vulenhtho.mrssso.dto.ItemDTO;
import com.vulenhtho.mrssso.dto.ProductDTO;
import com.vulenhtho.mrssso.dto.response.ItemShowInCartDTO;
import com.vulenhtho.mrssso.dto.response.ProductWebResponseDTO;
import com.vulenhtho.mrssso.dto.response.ProductWebWindowViewResponseDTO;
import com.vulenhtho.mrssso.entity.Color;
import com.vulenhtho.mrssso.entity.Product;
import com.vulenhtho.mrssso.entity.ProductColorSize;
import com.vulenhtho.mrssso.entity.Size;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    private final ColorRepository colorRepository;

    private final DiscountRepository discountRepository;

    private final SizeRepository sizeRepository;

    private final SubCategoryRepository subCategoryRepository;

    private final ProductColorSizeMapper productColorSizeMapper;

    private final ColorMapper colorMapper;

    private final SizeMapper sizeMapper;

    private final DiscountMapper discountMapper;

    private final SubCategoryMapper subCategoryMapper;

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
        Set<DiscountDTO> discountDTOSInIsActive = filterDiscountIsInDiscountTimeAndForProduct(discountMapper.toDTO(product.getDiscounts()));
        if (!CollectionUtils.isEmpty(discountDTOSInIsActive)) {
            productDTO.setDiscountDTOS(discountDTOSInIsActive);
            productDTO.setPrice(countPriceInDiscount(productDTO.getDiscountDTOS(), product.getPrice()));
            productDTO.setOriginalPrice(product.getPrice());

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

        Set<DiscountDTO> discountDTOSInIsActive = filterDiscountIsInDiscountTimeAndForProduct(discountMapper.toDTO(product.getDiscounts()));
        if (!CollectionUtils.isEmpty(discountDTOSInIsActive)) {

            responseDTO.setPrice(countPriceInDiscount(discountDTOS, product.getPrice()));
            responseDTO.setOriginalPrice(product.getPrice());
            responseDTO.setIsDiscount(true);

        }
        return responseDTO;
    }

    public ItemShowInCartDTO toItemShowInCartDTO(Product product, ItemDTO itemDTO) {
        if (product == null) return null;
        ItemShowInCartDTO itemResult = new ItemShowInCartDTO();
        BeanUtils.refine(product, itemResult, BeanUtils::copyNonNull);
        Optional<Color> colorName = product.getColors().stream().filter(color -> color.getId().equals(itemDTO.getColorId())).findFirst();
        Optional<Size> sizeName = product.getSizes().stream().filter(size -> size.getId().equals(itemDTO.getSizeId())).findFirst();

        itemResult.setColor(colorName.map(Color::getName).orElse(null));
        itemResult.setSize(sizeName.map(Size::getName).orElse(null));
        Set<DiscountDTO> discountDTOS = discountMapper.toDTO(product.getDiscounts());
        itemResult.setPrice(countPriceInDiscount(discountDTOS, product.getPrice()));
        itemResult.setImportPrice(product.getImportPrice());
        itemResult.setQuantity(itemDTO.getQuantity());
        itemResult.setTotalPrice(itemResult.getPrice() * itemResult.getQuantity());

        return itemResult;
    }

    public Long countPriceInDiscount(Set<DiscountDTO> discountDTOS, Long currentPrice) {
        Long finalPrice = currentPrice;
        for (DiscountDTO discountDTO : discountDTOS) {
            if (isInDiscountTimeAndForProduct(discountDTO)) {
                if (discountDTO.getAmount() != null) {
                    finalPrice -= discountDTO.getAmount();
                }
//                if (discountDTO.getPercent() != null) {
//                    finalPrice -= (currentPrice * discountDTO.getPercent() / 100);
//                }
            }

        }
        return finalPrice;
    }

    private boolean isInDiscountTimeAndForProduct(DiscountDTO discountDTO) {
        Instant now = Instant.now();
        return now.isAfter(discountDTO.getStartDate()) && now.isBefore(discountDTO.getEndDate()) && discountDTO.getIsForProduct();
    }

    private Set<DiscountDTO> filterDiscountIsInDiscountTimeAndForProduct(Set<DiscountDTO> discountDTOS) {
        return discountDTOS.stream().filter(this::isInDiscountTimeAndForProduct).collect(Collectors.toSet());
    }
}
