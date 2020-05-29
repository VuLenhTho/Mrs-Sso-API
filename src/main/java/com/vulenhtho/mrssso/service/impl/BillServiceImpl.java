package com.vulenhtho.mrssso.service.impl;

import com.vulenhtho.mrssso.dto.BillDTO;
import com.vulenhtho.mrssso.dto.DiscountDTO;
import com.vulenhtho.mrssso.dto.ItemDTO;
import com.vulenhtho.mrssso.dto.UpdateBillDTO;
import com.vulenhtho.mrssso.dto.request.BillFilterRequest;
import com.vulenhtho.mrssso.dto.request.CartDTO;
import com.vulenhtho.mrssso.dto.response.ItemShowInCartDTO;
import com.vulenhtho.mrssso.dto.response.ItemsForCartAndHeader;
import com.vulenhtho.mrssso.dto.response.ShortInfoBillResponse;
import com.vulenhtho.mrssso.entity.*;
import com.vulenhtho.mrssso.entity.enumeration.BillStatus;
import com.vulenhtho.mrssso.mapper.BillMapper;
import com.vulenhtho.mrssso.mapper.DiscountMapper;
import com.vulenhtho.mrssso.mapper.ProductMapper;
import com.vulenhtho.mrssso.repository.*;
import com.vulenhtho.mrssso.service.BillService;
import com.vulenhtho.mrssso.service.ProductService;
import com.vulenhtho.mrssso.specification.BillSpecification;
import com.vulenhtho.mrssso.util.BeanUtils;
import com.vulenhtho.mrssso.util.CommonUtils;
import com.vulenhtho.mrssso.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {

    private final ProductRepository productRepository;

    private final DiscountMapper discountMapper;

    private final BillRepository billRepository;

    private final ItemRepository itemRepository;

    private final ProductMapper productMapper;

    private final ColorRepository colorRepository;

    private final DiscountRepository discountRepository;

    private final SizeRepository sizeRepository;

    private final ProductService productService;

    private final BillMapper billMapper;

    private final SecurityUtils securityUtils;


    public BillServiceImpl(ProductRepository productRepository, DiscountMapper discountMapper, BillRepository billRepository, ItemRepository itemRepository, ProductMapper productMapper, ColorRepository colorRepository, DiscountRepository discountRepository, SizeRepository sizeRepository, ProductService productService, BillMapper billMapper, SecurityUtils securityUtils) {
        this.productRepository = productRepository;
        this.discountMapper = discountMapper;
        this.billRepository = billRepository;
        this.itemRepository = itemRepository;
        this.productMapper = productMapper;
        this.colorRepository = colorRepository;
        this.discountRepository = discountRepository;
        this.sizeRepository = sizeRepository;
        this.productService = productService;
        this.billMapper = billMapper;
        this.securityUtils = securityUtils;
    }

    @Override
    public ItemsForCartAndHeader getItemShowInCart(List<ItemDTO> itemDTOS) {
        List<ItemShowInCartDTO> itemShowInCartDTOS = itemDTOS.stream().map(itemDTO -> {
            Product product = productRepository.getOne(itemDTO.getProductId());
            return productMapper.toItemShowInCartDTO(product, itemDTO);
        }).collect(Collectors.toList());
        Set<DiscountDTO> discountDTOS = discountMapper.toDTO(discountRepository.getByInTimeDiscountAndForBill(Instant.now()));
        return new ItemsForCartAndHeader(itemShowInCartDTOS, discountDTOS, productService.getHeaderResponse());
    }

    @Override
    public void createBill(CartDTO cartDTO) {
        Bill billToSave = new Bill();

        BeanUtils.refine(cartDTO, billToSave, BeanUtils::copyNonNull);
        if (cartDTO.getAccountNumber() != null && cartDTO.getAccountName() != null) {
            billToSave.setPaymentInfo(cartDTO.getAccountName() + "," + cartDTO.getAccountNumber());
        }
        billToSave.setStatus(BillStatus.INIT);
        billToSave.setUser(securityUtils.getCurrentUserLogin());

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
            item.setImportPrice(product.getImportPrice());
            item.setBill(finalBillToSave);
            return item;
        }).collect(Collectors.toSet());

        itemRepository.saveAll(items);
    }

    @Override
    public Page<ShortInfoBillResponse> getListShortInfoBill(BillFilterRequest filterRequest) {
        return billRepository.findAll(BillSpecification.filterBill(filterRequest)
                , PageRequest.of(filterRequest.getPage()
                        , filterRequest.getSize()
                        , CommonUtils.getSort(filterRequest.getSort()))
        ).map(billMapper::toShortInfoBillResponse);
    }

    @Override
    public void delete(Long id) {
        Optional<Bill> bill = billRepository.findById(id);
        if (bill.isPresent()) {
            itemRepository.deleteAll(bill.get().getItems());
            billRepository.delete(bill.get());
        }
    }

    @Override
    public void delete(List<Long> ids) {
        ids.forEach(this::delete);
    }

    @Override
    public BillDTO getBillDetail(Long id) {
        Optional<Bill> bill = billRepository.findById(id);
        return bill.map(billMapper::toDTO).orElse(null);
    }

    @Override
    public void updateBillByAdmin(UpdateBillDTO updateBillDTO) {
        Bill currentBill = billRepository.findById(updateBillDTO.getBillDTO().getId()).get();
        setNewBillDataOnUpdate(currentBill, updateBillDTO);
        billRepository.save(currentBill);
    }

    private Bill setNewBillDataOnUpdate(Bill billToUpdate, UpdateBillDTO updateBillDTO) {
        BillDTO newBill = updateBillDTO.getBillDTO();
        billToUpdate.setAddress(newBill.getAddress());
        billToUpdate.setStatus(newBill.getStatus());
        billToUpdate.setReceiver(newBill.getReceiver());
        billToUpdate.setPhone(newBill.getPhone());
        billToUpdate.setPaymentMethod(newBill.getPaymentMethod());
        billToUpdate.setPaymentInfo(newBill.getPaymentInfo());
        billToUpdate.setNote(newBill.getNote());

        if (updateBillDTO.getProductIdsToDel() != null) {
            List<Long> productIdsToDel = Arrays.stream(updateBillDTO.getProductIdsToDel().split(","))
                    .filter(st -> !StringUtils.isEmpty(st))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            productIdsToDel.forEach(idToDel -> {
                billToUpdate.getItems().removeIf(item -> item.getProduct().getId().equals(idToDel));
            });
        }

        List<Long> quantityOfProducts = Arrays.stream(updateBillDTO.getQuantityOfProducts().split(","))
                .filter(st -> !StringUtils.isEmpty(st))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<Long> productIds = Arrays.stream(updateBillDTO.getProductIds().split(","))
                .filter(st -> !StringUtils.isEmpty(st))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        billToUpdate.getItems().forEach(item -> {
            for (int i = 0; i < productIds.size(); i++) {
                if (item.getProduct().getId().equals(productIds.get(i))) {
                    item.setQuantity(quantityOfProducts.get(i));
                }
            }
        });
        Long newFinalPayMoney = billToUpdate.getItems().stream().mapToLong(item -> item.getQuantity() * item.getPrice()).sum();
        Long newTotalImportPrice = billToUpdate.getItems().stream().mapToLong(item -> item.getQuantity() * item.getImportPrice()).sum();

        billToUpdate.setFinalPayMoney(newFinalPayMoney);
        billToUpdate.setTotalImportMoney(newTotalImportPrice);
        return billToUpdate;
    }

}
