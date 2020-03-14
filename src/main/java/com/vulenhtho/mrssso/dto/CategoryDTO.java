package com.vulenhtho.mrssso.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class CategoryDTO {
    private Long id;

    private String name;

    private Set<SubCategoryDTO> subCategoryDTOS = new HashSet<>();
}
