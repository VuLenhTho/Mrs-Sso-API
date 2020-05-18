package com.vulenhtho.mrssso.service.impl;

import com.vulenhtho.mrssso.config.Constant;
import com.vulenhtho.mrssso.dto.*;
import com.vulenhtho.mrssso.dto.request.CartDTO;
import com.vulenhtho.mrssso.dto.request.ItemDTO;
import com.vulenhtho.mrssso.dto.request.ProductFilterRequestDTO;
import com.vulenhtho.mrssso.dto.response.*;
import com.vulenhtho.mrssso.entity.*;
import com.vulenhtho.mrssso.mapper.CategoryMapper;
import com.vulenhtho.mrssso.mapper.DiscountMapper;
import com.vulenhtho.mrssso.mapper.ProductMapper;
import com.vulenhtho.mrssso.mapper.WelcomeSlideMapper;
import com.vulenhtho.mrssso.repository.*;
import com.vulenhtho.mrssso.service.ProductService;
import com.vulenhtho.mrssso.specification.ProductSpecification;
import com.vulenhtho.mrssso.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final ProductColorSizeRepository productColorSizeRepository;

    private final ColorRepository colorRepository;

    private final DiscountRepository discountRepository;

    private final SizeRepository sizeRepository;

    private final WelcomeSlideRepository welcomeSlideRepository;

    private final WelcomeSlideMapper welcomeSlideMapper;

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final DiscountMapper discountMapper;

    private final BillRepository billRepository;

    private final ItemRepository itemRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, ColorRepository colorRepository, DiscountRepository discountRepository, SizeRepository sizeRepository, SubCategoryRepository subCategoryRepository, ProductColorSizeRepository productColorSizeRepository, WelcomeSlideRepository welcomeSlideRepository, WelcomeSlideMapper welcomeSlideMapper, CategoryRepository categoryRepository, CategoryMapper categoryMapper, DiscountMapper discountMapper, BillRepository billRepository, ItemRepository itemRepository) {
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
        this.discountMapper = discountMapper;
        this.billRepository = billRepository;
        this.itemRepository = itemRepository;
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
        ProductWebResponseDTO productWebResponseDTO = product.map(value -> productMapper.toWebResponseDTO(value)).orElse(new ProductWebResponseDTO());
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
    public ItemsForCartAndHeader getItemShowInCart(List<ItemDTO> itemDTOS) {
        List<ItemShowInCartDTO> itemShowInCartDTOS = itemDTOS.stream().map(itemDTO -> {
            Product product = productRepository.getOne(itemDTO.getProductId());
            return productMapper.toItemShowInCartDTO(product, itemDTO);
        }).collect(Collectors.toList());
        Set<DiscountDTO> discountDTOS = discountMapper.toDTO(discountRepository.getByInTimeDiscountAndForBill(Instant.now()));
        return new ItemsForCartAndHeader(itemShowInCartDTOS, discountDTOS, getHeaderResponse());
    }

    @Override
    public void createBill(CartDTO cartDTO) {
        Bill billToSave = new Bill();

        BeanUtils.refine(cartDTO, billToSave, BeanUtils::copyNonNull);
        billToSave.setPaymentInfo(cartDTO.getAccountName() + "," + cartDTO.getAccountNumber());

        Bill finalBillToSave = billRepository.save(billToSave);
        Set<Item> items = cartDTO.getItemList().stream().map(itemDTO -> {
            Item item = new Item();
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            Product product = productRepository.getOne(itemDTO.getProductId());
            Color color = colorRepository.getOne(itemDTO.getColorId());
            Size size = sizeRepository.getOne(itemDTO.getSizeId());
            item.setColor(color.getName());
            item.setSize(size.getName());
            item.setProduct(product);
            item.setBill(finalBillToSave);
            return item;
        }).collect(Collectors.toSet());

        itemRepository.saveAll(items);

    }
}
