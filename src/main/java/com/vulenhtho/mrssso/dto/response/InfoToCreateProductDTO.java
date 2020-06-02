package com.vulenhtho.mrssso.dto.response;

import com.vulenhtho.mrssso.dto.DiscountDTO;
import com.vulenhtho.mrssso.dto.SubCategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoToCreateProductDTO {

    private Set<DiscountDTO> discountDTOS = new HashSet<>();

    private Set<SubCategoryDTO> subCategoryDTOS = new HashSet<>();
}
