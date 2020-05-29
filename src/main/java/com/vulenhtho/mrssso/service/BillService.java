package com.vulenhtho.mrssso.service;

import com.vulenhtho.mrssso.dto.BillDTO;
import com.vulenhtho.mrssso.dto.ItemDTO;
import com.vulenhtho.mrssso.dto.UpdateBillDTO;
import com.vulenhtho.mrssso.dto.request.BillFilterRequest;
import com.vulenhtho.mrssso.dto.request.CartDTO;
import com.vulenhtho.mrssso.dto.response.ItemsForCartAndHeader;
import com.vulenhtho.mrssso.dto.response.ShortInfoBillResponse;
import com.vulenhtho.mrssso.security.exception.ObjectIsNullException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BillService {

    ItemsForCartAndHeader getItemShowInCart(List<ItemDTO> itemDTOS);

    void createBill(CartDTO cartDTO);

    Page<ShortInfoBillResponse> getListShortInfoBill(BillFilterRequest filterRequest) throws ObjectIsNullException;

    void delete(List<Long> billIds);

    void delete(Long id);

    BillDTO getBillDetail(Long id);

    void updateBillByAdmin(UpdateBillDTO updateBillDTO);


}
