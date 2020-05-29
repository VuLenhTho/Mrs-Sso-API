package com.vulenhtho.mrssso.mapper;

import com.vulenhtho.mrssso.dto.ItemDTO;
import com.vulenhtho.mrssso.entity.Item;
import com.vulenhtho.mrssso.util.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public ItemDTO toDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        BeanUtils.refine(item, itemDTO, BeanUtils::copyNonNull);
        itemDTO.setProductId(item.getProduct().getId());
        itemDTO.setProductName(item.getProduct().getName());
        itemDTO.setThumbnail(item.getProduct().getThumbnail());
        itemDTO.setImportPrice(item.getImportPrice());
        return itemDTO;
    }
}
